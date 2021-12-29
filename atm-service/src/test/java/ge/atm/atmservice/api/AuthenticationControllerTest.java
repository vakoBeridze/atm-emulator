package ge.atm.atmservice.api;

import ge.atm.atmservice.domain.AuthenticationRequest;
import ge.atm.atmservice.domain.AuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static ge.atm.atmservice.config.JsonConverter.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthenticationControllerTest extends BaseControllerTest {

    @Test
    void initiateAuthentication() throws Exception {
        // Given
        final AuthenticationRequest request = AuthenticationRequest.builder().cardNumber("12345678").build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder().build();
        doReturn(mockResponse).when(authenticationService).initiateAuthentication(any());

        // When
        // Then
        mockMvc.perform(post("/api/authentication/initiate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void finalizeAuthenticationUnauthroized() throws Exception {
        // Given
        final AuthenticationRequest request = AuthenticationRequest.builder().cardNumber("12345678").secret("1234").build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder().build();
        doReturn(mockResponse).when(authenticationService).finalizeAuthentication(any());

        // When
        // Then
        mockMvc.perform(post("/api/authentication/finalize")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "12345678")
    void finalizeAuthentication() throws Exception {
        // Given
        final AuthenticationRequest request = AuthenticationRequest.builder().cardNumber("12345678").secret("1234").build();
        AuthenticationResponse mockResponse = AuthenticationResponse.builder().build();
        doReturn(mockResponse).when(authenticationService).finalizeAuthentication(any());

        // When
        // Then
        mockMvc.perform(post("/api/authentication/finalize")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}