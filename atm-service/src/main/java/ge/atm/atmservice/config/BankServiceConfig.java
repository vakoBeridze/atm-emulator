package ge.atm.atmservice.config;

import ge.atm.bankservice.ApiClient;
import ge.atm.bankservice.api.BankAccountControllerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankServiceConfig {

    @Value("${bank-service.base-path}")
    private String bankServiceBasePath;

    @Value("${bank-service.auth-token}")
    private String bankServiceApiKey;

    @Bean
    public BankAccountControllerApi bankAccountControllerApi() {
        return new BankAccountControllerApi(apiClient());
    }

    private ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.addDefaultHeader("X-API-KEY", bankServiceApiKey);
        apiClient.setBasePath(bankServiceBasePath);
        return apiClient;
    }
}
