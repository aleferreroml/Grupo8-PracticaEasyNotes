package com.example.easynotes.integration;

import com.example.easynotes.dto.CategoriaUsuario;
import com.example.easynotes.dto.UserResponseWithCategoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    private static ObjectWriter writer;

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void initData() {
        writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                // esto es para fechas
                .registerModule(new JSR310Module())
                .writer()
                .withDefaultPrettyPrinter();
    }

    @Test
    public void testCorrectGetCategoryUser() throws Exception {
        Long id = 1L;

        UserResponseWithCategoryDTO expectedRes = new UserResponseWithCategoryDTO();
        expectedRes.setCategory(CategoriaUsuario.Publicador);
        expectedRes.setId(id);
        expectedRes.setFirstName("user1");
        expectedRes.setLastName("last1");

        String expectedCategory = writer.writeValueAsString(expectedRes);

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedJson = content().json(expectedCategory);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/user/category/{id}", id))
                .andDo(print())
                .andExpectAll(expectedStatus, expectedContentType, expectedJson);
    }
}
