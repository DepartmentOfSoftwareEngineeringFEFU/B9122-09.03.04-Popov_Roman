package plugin.prep.auth.controllers;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import org.junit.jupiter.api.*;

import plugin.prep.auth.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class AuthControllerTest extends AbstractIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void register_and_login_success() throws Exception {
        var login = "user-" + UUID.randomUUID();
        var password = "qwerty123";

        var body = """
            {"email":"%s@example.com", "login":"%s", "password":"%s", "learningTrackId":1}
            """.formatted(login, login, password);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", not(blankOrNullString())))
            .andExpect(jsonPath("$.refreshToken", not(blankOrNullString())));

        var loginBody = """
            {"login":"%s", "password":"%s"}
            """.formatted(login, password);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", not(blankOrNullString())))
            .andExpect(jsonPath("$.refreshToken", not(blankOrNullString())));
    }

    @Test
    public void logout_success() throws Exception {
        var login = "user-" + UUID.randomUUID();

        var body = """
            {"email":"%s@example.com", "login":"%s", "password":"qwerty123", "learningTrackId":1}
            """.formatted(login, login);

        var response = mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var refreshToken = objectMapper
            .readTree(response)
            .get("refreshToken")
            .asText();

        var tokenBody = """
            {"token":"%s"}
            """.formatted(refreshToken);

        mvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tokenBody))
            .andExpect(status().isOk());

        mvc.perform(post("/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tokenBody))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void register_duplicate_login_or_email_conflict() throws Exception {
        var login = "user-" + UUID.randomUUID();

        var body = """
            {"email":"%s@example.com", "login":"%s", "password":"qwerty123", "learningTrackId":1}
            """.formatted(login, login);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

    @Test
    public void register_validation_error() throws Exception {
        var body = """
            {"email":"bad-email", "login":"", "password":"short"}
            """;

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

    @Test
    public void register_unknown_learning_track_bad_request() throws Exception {
        var login = "user-" + UUID.randomUUID();

        var body = """
            {"email":"%s@example.com", "login":"%s", "password":"qwerty123", "learningTrackId":999}
            """.formatted(login, login);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", not(blankOrNullString())));
    }

}
