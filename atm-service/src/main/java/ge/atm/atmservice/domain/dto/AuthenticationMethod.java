package ge.atm.atmservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum AuthenticationMethod {

    PIN(1L),
    FINGERPRINT(2L);

    private final long databaseId;
    private static final Map<Long, AuthenticationMethod> ID_LOOKUP_MAP = Arrays.stream(AuthenticationMethod.values()).collect(Collectors.toMap(AuthenticationMethod::getDatabaseId, Function.identity()));


    /**
     * Look up the AuthenticationMethod enum by its database ID
     *
     * @param id AuthenticationMethod database ID
     * @return corresponding AuthenticationMethod enum
     * @throws IllegalArgumentException if the ID is not valid/known
     */
    public static AuthenticationMethod valueOf(long id) {
        final AuthenticationMethod authenticationMethod = ID_LOOKUP_MAP.get(id);

        if (authenticationMethod == null) {
            throw new IllegalArgumentException("Invalid authentication method type ID: " + id);
        }

        return authenticationMethod;
    }
}
