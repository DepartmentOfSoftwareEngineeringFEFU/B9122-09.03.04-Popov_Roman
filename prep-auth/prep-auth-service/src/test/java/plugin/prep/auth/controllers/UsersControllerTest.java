package plugin.prep.auth.controllers;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import org.junit.jupiter.api.*;

import plugin.prep.auth.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class UsersControllerTest extends AbstractIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void me_without_token_unauthorized() throws Exception {
        mvc.perform(get("/users/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void me_success() throws Exception {
        var login = "user-" + UUID.randomUUID();
        var email = login + "@example.com";

        var body = """
            {"email":"%s", "login":"%s", "password":"qwerty123", "learningTrackId":1}
            """.formatted(email, login);

        var response = mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var accessToken = objectMapper
            .readTree(response)
            .get("accessToken")
            .asText();

        mvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.login").value(login))
            .andExpect(jsonPath("$.grade").doesNotExist())
            .andExpect(jsonPath("$.learningTrackId").value(1))
            .andExpect(jsonPath("$.role").value("USER"));
    }

}
