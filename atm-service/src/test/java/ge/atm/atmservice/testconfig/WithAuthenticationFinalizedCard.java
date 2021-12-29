package ge.atm.atmservice.testconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomCard(cardNumber = "1111222233334444", roles = "FINALIZED_AUTHENTICATION")
public @interface WithAuthenticationFinalizedCard {
}
