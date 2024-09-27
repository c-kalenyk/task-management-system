package mate.academy.taskmanagementsystem.controller;

import static mate.academy.taskmanagementsystem.util.CommentTestUtil.createTestComment;
import static mate.academy.taskmanagementsystem.util.CommentTestUtil.createTestCommentDto;
import static mate.academy.taskmanagementsystem.util.CommentTestUtil.createTestCreateCommentRequestDto;
import static mate.academy.taskmanagementsystem.util.CommentTestUtil.fillExpectedCommentDtoList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.dto.comment.CommentDto;
import mate.academy.taskmanagementsystem.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementsystem.model.Comment;
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
public class CommentControllerTest {
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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/tasks/add-task.sql")
            );
        }
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Create a new comment")
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createComment_ValidRequestDto_ShouldCreateComment() throws Exception {
        //Given
        Comment comment = createTestComment();
        CreateCommentRequestDto requestDto = createTestCreateCommentRequestDto(comment);
        CommentDto expected = createTestCommentDto(comment);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/comments")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        CommentDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CommentDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Get a list of all comments for a task")
    @Sql(scripts = {
            "classpath:database/comments/add-three-comments.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/comments/delete-comments.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllComments_GivenComments_ShouldReturnCommentDtoList()
            throws Exception {
        //Given
        List<CommentDto> expected = fillExpectedCommentDtoList();
        //When
        MvcResult result = mockMvc.perform(
                get("/comments")
                .param("taskId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CommentDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CommentDto[].class
        );
        assertEquals(3, actual.length);
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
                    new ClassPathResource("database/tasks/delete-tasks.sql")
            );
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
