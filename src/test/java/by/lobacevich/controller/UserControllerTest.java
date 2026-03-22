package by.lobacevich.controller;

import by.lobacevich.util.TestAuthUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends BaseIntegrationTest {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser_ShouldCreateUserAndReturnUserDtoResponse() throws Exception {
        String createJson = """
                {
                  "name": "John",
                  "surname": "Doe",
                  "birthDate": "1990-01-01",
                  "email": "john@test.com"
                }
                """;
        mockMvc.perform(post("/users")
                        .with(TestAuthUtil.user(1L, ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void update_ShouldReturnUpdatedUserDtoResponse() throws Exception {
        String updateJson = """
                {
                  "name": "Alex",
                  "surname": "Doe",
                  "birthDate": "1990-01-01",
                  "email": "alex@test.com"
                }
                """;
        mockMvc.perform(put("/users/1")
                        .with(TestAuthUtil.user(1L, USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.email").value("alex@test.com"));
    }

    @Test
    void deactivate_ShouldSetActiveFieldFalse() throws Exception {
        mockMvc.perform(patch("/users/1/deactivate")
                        .with(TestAuthUtil.user(1L, ADMIN)))
                .andExpect(status().isOk());
    }

    @Test
    void activate_ShouldSetActiveFieldTrue() throws Exception {
        mockMvc.perform(patch("/users/1/activate")
                        .with(TestAuthUtil.user(1L, ADMIN)))
                .andExpect(status().isOk());
    }

    @Test
    void findById_ShouldReturnUserWithCards() throws Exception {
        mockMvc.perform(get("/users/1")
                        .with(TestAuthUtil.user(1L, USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentCards").isArray());
    }

    @Test
    void findById_ShouldReturnUnauthorizedStatus() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_ShouldReturnForbiddenStatus() throws Exception {
        mockMvc.perform(get("/users/1")
                        .with(TestAuthUtil.user(2L, USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void findAll_ShouldReturnPageWithUserDtoResponse() throws Exception {
        mockMvc.perform(get("/users")
                        .with(TestAuthUtil.user(1L, ADMIN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
