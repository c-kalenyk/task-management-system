package mate.academy.taskmanagementsystem.controller;

import static mate.academy.taskmanagementsystem.util.LabelTestUtil.createTestLabel;
import static mate.academy.taskmanagementsystem.util.LabelTestUtil.createTestLabelRequestDto;
import static mate.academy.taskmanagementsystem.util.LabelTestUtil.fillExpectedLabelList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.taskmanagementsystem.config.GlobalSetupExtension;
import mate.academy.taskmanagementsystem.dto.label.LabelRequestDto;
import mate.academy.taskmanagementsystem.model.Label;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(GlobalSetupExtension.class)
public class LabelControllerTest {
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
    @WithMockUser(username = "user")
    @DisplayName("Create a new label")
    @Sql(scripts = {
            "classpath:database/labels/delete-labels.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void createLabel_ValidRequestDto_ShouldCreateLabel() throws Exception {
        //Given
        Label expected = createTestLabel();
        LabelRequestDto requestDto = createTestLabelRequestDto(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                post("/labels")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        Label actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), Label.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get a list of all available labels")
    @Sql(scripts = {
            "classpath:database/labels/add-three-labels.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/labels/delete-labels.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void getAllLabels_GivenLabels_ShouldReturnAllLabels()
            throws Exception {
        //Given
        List<Label> expected = fillExpectedLabelList();
        //When
        MvcResult result = mockMvc.perform(
                get("/labels")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Label[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), Label[].class
        );
        assertEquals(3, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get label by id")
    @Sql(scripts = {
            "classpath:database/labels/add-one-label.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/labels/delete-labels.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void updateLabel_ValidRequestDto_ShouldUpdateLabel() throws Exception {
        //Given
        Label expected = createTestLabel();
        expected.setName("updated name");
        LabelRequestDto requestDto = createTestLabelRequestDto(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //When
        MvcResult result = mockMvc.perform(
                put("/labels/1")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Label actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), Label.class
        );
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Delete label by id")
    @Sql(scripts = {
            "classpath:database/labels/add-three-labels.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {
            "classpath:database/labels/delete-labels.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteLabel_GivenLabels_ShouldDeleteLabel() throws Exception {
        //Given
        List<Label> expected = fillExpectedLabelList();
        expected.remove(1);
        //When
        mockMvc.perform(
                delete("/labels/1")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
        MvcResult result = mockMvc.perform(
                get("/labels")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        //Then
        Label[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), Label[].class
        );
        assertEquals(2, actual.length);
        EqualsBuilder.reflectionEquals(expected, Arrays.stream(actual).toList());
    }
}
