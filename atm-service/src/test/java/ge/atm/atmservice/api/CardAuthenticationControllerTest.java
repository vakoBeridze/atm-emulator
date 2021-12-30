package ge.atm.atmservice.api;

import ge.atm.atmservice.testconfig.WithAuthenticationFinalizedCard;
import ge.atm.bankservice.domain.dto.CardDto;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CardAuthenticationControllerTest extends BaseControllerTest {

    @Test
    @WithAuthenticationFinalizedCard
    void updatePreferredAuth() throws Exception {

        // Given
        doNothing().when(cardService).updatePreferredAuth(CardDto.PreferredAuthEnum.FINGERPRINT);

        // When
        // Then
        mockMvc.perform(put("/api/card/preferred-auth")
                        .param("preferredAuth", CardDto.PreferredAuthEnum.FINGERPRINT.name()))
                .andExpect(status().isOk());
    }
}