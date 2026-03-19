package by.lobacevich.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class PaymentCardControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCard_ShouldCreateCardAndReturnPayCardDtoResponse() throws Exception {
        String createJson = """
                {
                  "userId": 2,
                  "number": "1234567812345678",
                  "holder": "John Doe",
                  "expirationDate": "2030-01-01"
                }
                """;
        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("1234567812345678"))
                .andExpect(jsonPath("$.holder").value("John Doe"));
    }

    @Test
    void update_ShouldReturnUpdatedPayCardDtoResponse() throws Exception {
        String updateJson = """
                {
                  "userId": 2,
                  "number": "1114567812341111",
                  "holder": "John Smith",
                  "expirationDate": "2030-01-01"
                }
                """;
        mockMvc.perform(put("/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("1114567812341111"))
                .andExpect(jsonPath("$.holder").value("John Smith"));
    }

    @Test
    void deactivate_ShouldSetActiveFieldFalse() throws Exception {
        mockMvc.perform(patch("/cards/1/deactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void activate_ShouldSetActiveFieldTrue() throws Exception {
        mockMvc.perform(patch("/cards/1/activate"))
                .andExpect(status().isOk());
    }

    @Test
    void findByUserId_ShouldReturnListOfPayCardDtoResponse() throws Exception {
        mockMvc.perform(get("/cards/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findById_ShouldReturnUserWithCards() throws Exception {
        mockMvc.perform(get("/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").isNotEmpty());
    }

    @Test
    void findAll_ShouldReturnPageWithUserDtoResponse() throws Exception {
        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
