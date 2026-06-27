package plugin.prep.example_service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.*;
import org.springframework.security.web.*;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationsTest {

    /**
     * Валиден 10 лет
     */
    private String token =
        "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIwY2ZjYzkwYy01MjAwL" +
        "TQxY2ItYWIxNy01MzM4NTU2ZmEwNzIiLCJ1c2VyX2lkIjoxMjMsInJvbGVzIjpbIlV" +
        "TRVIiXSwiaWF0IjoxNzQ1MTA4MTMwLCJleHAiOjIwNjA2NDA5MzB9.Uw-0PasG_Lrp" +
        "5K0tP4HfiTkTk-_IQP5hYDNQpYBgURuyQL4cvWTHSBWT4Mb_J5ewbJKZYCChR5EdJQ" +
        "1R1mwdDGGxvee5Yq-_8ViATI7lLxwYDfiumjZR-3l734S3E_wEMsITnuTA3GzAN34U" +
        "DSROiZzfUEm0DR2nAd-5lVasLid1lCZ__SH2nE4baXeRgus2ivg5nWc7pJEQf00Qm4" +
        "cRkL-Pa2HrLmelFqoS6Ce4JdikoCfUXqLxavRqL5Gq7pyA6EvA0JUef6ZABDS9BIcD" +
        "2DGbLYJeLDKNjAph6zVtA4oeF4imL-doQllBqouIkqUi5h7DT03zl0lJlUVAZJDGOg";

    @Autowired
    protected SecurityFilterChain filterChain;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void get_open_endloint() throws Exception {
        mockMvc.perform(get("/open"))
            .andExpect(status().isOk());
    }

    @Test
    public void get_closed_endloint_authorized() throws Exception {
        mockMvc.perform(get("/secure")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    public void get_closed_endloint_unauthorized() throws Exception {
        mockMvc.perform(get("/secure")
                .header("Authorization", "Bearer " + token + "1"))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

}
