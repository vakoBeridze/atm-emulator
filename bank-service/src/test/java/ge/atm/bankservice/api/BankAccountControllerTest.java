package ge.atm.bankservice.api;

import ge.atm.bankservice.domain.dto.AuthenticationMethod;
import ge.atm.bankservice.domain.dto.CardDto;
import ge.atm.bankservice.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BankAccountControllerTest {

    @MockBean
    protected BankAccountService bankAccountService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    @WithMockUser(username = "12345678")
    void getCard() throws Exception {
        // Given
        CardDto mockResponse = new CardDto();
        mockResponse.setCardNumber("1234");
        mockResponse.setBalance(BigDecimal.valueOf(100));
        doReturn(mockResponse).when(bankAccountService).getCard("1234");

        // When
        // Then
        mockMvc.perform(get("/api/card/1234"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cardNumber", equalTo("1234")))
                .andExpect(jsonPath("$.balance", equalTo(100)));
    }

    @Test
    @WithMockUser(username = "12345678")
    void validateCardSecretBadRequest() throws Exception {
        // Given
        doNothing().when(bankAccountService).validateCardSecret("1234", "1111", AuthenticationMethod.PIN);

        // When
        // Then
        mockMvc.perform(post("/api/card/1234/validate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "12345678")
    void validateCardSecret() throws Exception {
        // Given
        doNothing().when(bankAccountService).validateCardSecret("1234", "1111", AuthenticationMethod.PIN);

        // When
        // Then
        mockMvc.perform(post("/api/card/1234/validate")
                        .param("secret", "1111")
                        .param("preferredAuth", "PIN"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "12345678")
    void updatePreferredAuth() throws Exception {
        // Given
        doNothing().when(bankAccountService).updatePreferredAuth("1234", AuthenticationMethod.PIN);

        // When
        // Then
        mockMvc.perform(put("/api/card/1234/preferred-auth")
                        .param("preferredAuth", "PIN"))
                .andExpect(status().isOk());
    }

    @Test
    void getCardBalanceForbidden() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(bankAccountService).getCurrentBalance("1234");

        // When
        // Then
        mockMvc.perform(get("/api/card/1234/balance"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "12345678")
    void getCardBalance() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(bankAccountService).getCurrentBalance("1234");

        // When
        // Then
        mockMvc.perform(get("/api/card/1234/balance"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(10)));
    }

    @Test
    @WithMockUser(username = "12345678")
    void depositCardBalance() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(bankAccountService).depositCardBalance("1234", BigDecimal.TEN);

        // When
        // Then
        mockMvc.perform(put("/api/card/1234/deposit")
                        .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(10)));
    }

    @Test
    @WithMockUser(username = "12345678")
    void withdrawCardBalance() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.ZERO;
        doReturn(mockResponse).when(bankAccountService).withdrawCardBalance("1234", BigDecimal.TEN);

        // When
        // Then
        mockMvc.perform(put("/api/card/1234/withdraw")
                        .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(0)));
    }

    @Test
    void withdrawCardBalanceForbidden() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.ZERO;
        doReturn(mockResponse).when(bankAccountService).withdrawCardBalance("1234", BigDecimal.TEN);

        // When
        // Then
        mockMvc.perform(put("/api/card/1234/withdraw")
                        .param("amount", "10"))
                .andExpect(status().isForbidden());
    }
}