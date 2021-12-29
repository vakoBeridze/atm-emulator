package ge.atm.atmservice.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@ApiModel(description = "Authentication Request")
@Builder
@Validated
public class AuthenticationRequest implements Serializable {

    @ApiModelProperty(value = "Card number", required = true, example = "1111222233334444")
    private String cardNumber;

    @ApiModelProperty(value = "Preferred authentication method secret", example = "1234")
    private String secret;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
