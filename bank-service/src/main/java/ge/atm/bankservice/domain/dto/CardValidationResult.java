package ge.atm.bankservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardValidationResult {
    private String errorMessage;

    public boolean hasError() {
        return errorMessage != null;
    }
}
