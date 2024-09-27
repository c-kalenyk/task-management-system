package mate.academy.taskmanagementsystem.controller;

import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTask;
import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTaskDto;
import static mate.academy.taskmanagementsystem.util.TaskTestUtil.createTestTaskRequestDto;
import static mate.academy.taskmanagementsystem.util.TaskTestUtil.fillExpectedTaskDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.dto.task.TaskDto;
import mate.academy.taskmanagementsystem.dto.task.TaskRequestDto;
import mate.academy.taskmanagementsystem.model.Task;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(GlobalSetupExtension.class)
public class TaskControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/roles/add-roles.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/add-two-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users_roles/set-roles.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/projects/add-project.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/projects_users/set-user-for-project.sql")
            );
        }
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Create a new task")
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createTask_ValidRequestDto_ShouldCreateTask() throws Exception {
        //Given
        Task task = createTestTask();
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        TaskDto expected = createTestTaskDto(task);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/tasks")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        TaskDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), TaskDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Get a list of all project's tasks")
    @Sql(scripts = {
            "classpath:database/tasks/add-two-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllTasks_GivenTasks_ShouldReturnTaskDtoList()
            throws Exception {
        //Given
        List<TaskDto> expected = fillExpectedTaskDtoList();
        //When
        MvcResult result = mockMvc.perform(
                get("/tasks/project/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        TaskDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), TaskDto[].class
        );
        assertEquals(2, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Get task by id")
    @Sql(scripts = {
            "classpath:database/tasks/add-task.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getTask_GivenTask_ShouldReturnTaskDto() throws Exception {
        //Given
        TaskDto expected = createTestTaskDto(createTestTask());
        //When
        MvcResult result = mockMvc.perform(
                get("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        TaskDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), TaskDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Update task by id")
    @Sql(scripts = {
            "classpath:database/tasks/add-task.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateTask_ValidRequestDto_ShouldUpdateTask() throws Exception {
        //Given
        Task task = createTestTask();
        task.setDescription("upd description");
        TaskRequestDto requestDto = createTestTaskRequestDto(task);
        TaskDto expected = createTestTaskDto(task);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/tasks/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        TaskDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), TaskDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Delete task by id")
    @Sql(scripts = {
            "classpath:database/tasks/add-two-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/tasks/delete-tasks.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteTask_GivenTasks_ShouldDeleteTask() throws Exception {
        //Given
        List<TaskDto> expected = fillExpectedTaskDtoList();
        expected.removeLast();
        //When
        mockMvc.perform(
                delete("/tasks/2")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/tasks/project/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        TaskDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), TaskDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/projects_users/remove-users-from-projects.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/projects/delete-projects.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users_roles/remove-roles-from-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/delete-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/roles/delete-roles.sql")
            );
        }
    }
}
