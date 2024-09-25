package mate.academy.taskmanagementsystem.controller;

import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUser;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUserRegistrationRequestDto;
import static mate.academy.taskmanagementsystem.util.UserTestUtil.createTestUserResponseDto;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.dto.user.UserRegistrationRequestDto;
import mate.academy.taskmanagementsystem.dto.user.UserResponseDto;
import mate.academy.taskmanagementsystem.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(GlobalSetupExtension.class)
public class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("bob")
    @DisplayName("Get current user's info")
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql",
            "classpath:database/roles/add-roles.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/users_roles/set-admin-role-for-bob.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_roles/remove-roles-from-users.sql",
            "classpath:database/users/delete-users.sql",
            "classpath:database/roles/delete-roles.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getUserInfo_GivenUser_ShouldReturnUserResponseDto() throws Exception {
        //Given
        User user = createTestUser();
        UserResponseDto expected = createTestUserResponseDto(user);
        //When
        MvcResult result = mockMvc.perform(
                        get("/users/me")
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
    @DisplayName("Update current user's info")
    @Sql(scripts = {
            "classpath:database/roles/delete-roles.sql",
            "classpath:database/roles/add-roles.sql",
            "classpath:database/users/add-user.sql",
            "classpath:database/users_roles/set-admin-role-for-bob.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/users_roles/remove-roles-from-users.sql",
            "classpath:database/users/delete-users.sql",
            "classpath:database/roles/delete-roles.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateUserInfo_GivenValidRequestDto_ShouldReturnUserResponseDto() throws Exception {
        //Given
        User user = createTestUser();
        user.setFirstName("upd name");
        UserRegistrationRequestDto requestDto = createTestUserRegistrationRequestDto(user);
        UserResponseDto expected = createTestUserResponseDto(user);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                        put("/users/me")
                                .content(jsonRequest)
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
}
