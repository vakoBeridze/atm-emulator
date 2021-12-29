package ge.atm.atmservice.testconfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockCustomUser(username = "1111222233334444", name = "Vako Beridze", roles = "FINALIZED_AUTHENTICATION")
public @interface WithAuthenticationFinalizedUser {
}
