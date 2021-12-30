package ge.atm.atmservice.api;

import ge.atm.bankservice.domain.dto.CardDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardAuthenticationControllerTest extends BaseControllerTest {

    @Test
    void updatePreferredAuth() throws Exception {

        // Given
        BigDecimal mockResponse = BigDecimal.TEN;
        doReturn(mockResponse).when(cardService).updatePreferredAuth(CardDto.PreferredAuthEnum.FINGERPRINT);

        // When
        // Then
        mockMvc.perform(put("/api/card/preferred-auth"))
                .andExpect(status().isOk());
    }
}