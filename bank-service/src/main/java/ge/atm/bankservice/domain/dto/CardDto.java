package ge.atm.bankservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {

    private long id;
    private String cardNumber;
    private AuthenticationMethod preferredAuth;
}
