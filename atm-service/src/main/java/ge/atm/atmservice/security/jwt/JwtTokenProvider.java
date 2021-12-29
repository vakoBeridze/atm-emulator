package ge.atm.atmservice.security.jwt;

import ge.atm.atmservice.domain.dto.AuthenticationMethod;
import ge.atm.atmservice.security.Roles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for generating access tokens.
 * Access tokens are credentials used to access protected resources.
 *
 * @see https://tools.ietf.org/html/rfc6749#section-1.4
 */
@Slf4j
@Component
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";
    private final String jwtSecret;

    /**
     * This option controls the maximum time that the access token is active,
     * This property is also used to calculate JWT "exp" claim.
     * The "exp" (expiration time) claim identifies the expiration time on
     * or after which the JWT MUST NOT be accepted for processing.
     */
    private final int expirationInSeconds;

    public JwtTokenProvider(@Value("${jwt.token.secret}") String jwtSecret,
                            @Value("${jwt.token.expirationInSeconds}") int expirationInSeconds) {

        this.jwtSecret = jwtSecret;
        this.expirationInSeconds = expirationInSeconds;
    }

    /**
     * Generates access token by given subject and roles.
     *
     * @param subject as card number
     * @return BASE64 encoded JWT access token.
     */
    public String generateToken(String subject, AuthenticationMethod authMethod, Boolean finalized) {
        Date now = new Date();
        int expirationInMillis = this.expirationInSeconds * 1000;
        Date expirationDate = new Date(now.getTime() + expirationInMillis);

        Claims claims = Jwts.claims();
        claims.setSubject(subject);
        claims.put("preferred_auth", authMethod);
        claims.put("roles", finalized ? Collections.singletonList(Roles.ROLE_FINALIZED_AUTHENTICATION) : Collections.emptyList());

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public JwtCardDetails getCardDetailsFromJwt(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        final String preferredAuth = (String) claims.get("preferred_auth");
        final List<String> roles = (List<String>) claims.get("roles");
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new JwtCardDetails(claims.getSubject(), AuthenticationMethod.valueOf(preferredAuth), authorities);
    }

    public String resolveToken(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(TOKEN_PREFIX)) {
            return accessToken.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException |
                ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.debug("invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    public Key getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return new SecretKeySpec(bytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public Authentication getAuthentication(String token) {
        final JwtCardDetails cardDetails = this.getCardDetailsFromJwt(token);
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(cardDetails, "", cardDetails.getAuthorities());
        authenticationToken.setDetails(cardDetails.getPreferredAuthenticationMethod());
        return authenticationToken;
    }

}
