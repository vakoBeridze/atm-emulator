package ge.atm.atmservice.api;

import ge.atm.atmservice.testconfig.WithAuthenticationFinalizedCard;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardBalanceControllerTest extends BaseControllerTest {

    @Test
    void getCurrentBalanceUnauthorized() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(cardBalanceService).getCurrentBalance();

        // When
        // Then
        mockMvc.perform(get("/api/balance"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "12345678")
    void getCurrentBalanceForbidden() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(cardBalanceService).getCurrentBalance();

        // When
        // Then
        mockMvc.perform(get("/api/balance"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAuthenticationFinalizedCard
    void getCurrentBalance() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(cardBalanceService).getCurrentBalance();

        // When
        // Then
        mockMvc.perform(get("/api/balance"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(10)));
    }

    @Test
    @WithAuthenticationFinalizedCard
    void depositCardBalance() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(cardBalanceService).depositCardBalance(BigDecimal.TEN);

        // When
        // Then
        mockMvc.perform(post("/api/balance/deposit")
                        .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(10)));
    }

    @Test
    @WithAuthenticationFinalizedCard
    void withdrawCardBalance() throws Exception {
        // Given
        BigDecimal mockResponse = BigDecimal.ZERO;
        doReturn(mockResponse).when(cardBalanceService).withdrawCardBalance(BigDecimal.TEN);

        // When
        // Then
        mockMvc.perform(post("/api/balance/withdraw")
                        .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", equalTo(0)));
    }
}