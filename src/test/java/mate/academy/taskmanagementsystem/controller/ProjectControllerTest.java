package mate.academy.taskmanagementsystem.controller;

import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestCreateProjectRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProject;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProjectDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestProjectUserAssignmentRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestUpdateProjectRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.createTestUpdateProjectStatusRequestDto;
import static mate.academy.taskmanagementsystem.util.ProjectTestUtil.updateProjectInfo;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUsersSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectDto;
import mate.academy.taskmanagementsystem.dto.project.ProjectUserAssignmentRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectRequestDto;
import mate.academy.taskmanagementsystem.dto.project.UpdateProjectStatusRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.model.Project;
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
public class ProjectControllerTest {
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
        }
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Create a new project")
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createProject_ValidRequestDto_ShouldCreateProject() throws Exception {
        //Given
        Project project = createTestProject();
        CreateProjectRequestDto requestDto = createTestCreateProjectRequestDto(project);
        ProjectDto expected = createTestProjectDto(project);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/projects")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        ProjectDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProjectDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Get a set of all user's projects")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllProjects_GivenProject_ShouldReturnProjectDtoSet()
            throws Exception {
        //Given
        Set<ProjectDto> expected = Set.of(createTestProjectDto(createTestProject()));
        //When
        MvcResult result = mockMvc.perform(
                get("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ProjectDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ProjectDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).collect(Collectors.toSet()));
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Get project by id")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getProject_GivenProject_ShouldReturnProjectDto() throws Exception {
        //Given
        ProjectDto expected = createTestProjectDto(createTestProject());
        //When
        MvcResult result = mockMvc.perform(
                get("/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Update project by id")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateProject_ValidRequestDto_ShouldUpdateProject() throws Exception {
        //Given
        Project existingProject = createTestProject();
        UpdateProjectRequestDto requestDto = createTestUpdateProjectRequestDto();
        Project updatedProject = updateProjectInfo(requestDto, existingProject);
        ProjectDto expected = createTestProjectDto(updatedProject);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/projects/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ProjectDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProjectDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Update project status by id")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateStatus_ValidRequestDto_ShouldUpdateProjectStatus() throws Exception {
        //Given
        Project project = createTestProject();
        UpdateProjectStatusRequestDto requestDto = createTestUpdateProjectStatusRequestDto();
        project.setStatus(requestDto.getStatus());
        ProjectDto expected = createTestProjectDto(project);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                patch("/projects/1/status")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ProjectDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProjectDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Assign additional user to the project")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void assignUser_ValidRequestDto_ShouldAddUserToProject() throws Exception {
        //Given
        Project project = createTestProject();
        ProjectUserAssignmentRequestDto requestDto = createTestProjectUserAssignmentRequestDto();
        project.setUsers(createTestUsersSet());
        ProjectDto expected = createTestProjectDto(project);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/projects/1/users")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ProjectDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProjectDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Unassign user from the project")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects_users/set-two-users-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void removeUser_ValidRequestDto_ShouldRemoveUserFromProject() throws Exception {
        //Given
        Project project = createTestProject();
        ProjectUserAssignmentRequestDto requestDto = createTestProjectUserAssignmentRequestDto();
        ProjectDto expected = createTestProjectDto(project);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                patch("/projects/1/users")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ProjectDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ProjectDto.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Delete project by id")
    @Sql(scripts = {
            "classpath:database/projects/add-project.sql",
            "classpath:database/projects/add-extra-project.sql",
            "classpath:database/projects_users/set-user-for-project.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/projects_users/remove-users-from-projects.sql",
            "classpath:database/projects/delete-projects.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteLabel_GivenLabels_ShouldDeleteLabel() throws Exception {
        //Given
        Set<ProjectDto> expected = Set.of(createTestProjectDto(createTestProject()));
        //When
        mockMvc.perform(
                delete("/projects/2")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        ProjectDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), ProjectDto[].class
        );
        assertEquals(1, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).collect(Collectors.toSet()));
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
