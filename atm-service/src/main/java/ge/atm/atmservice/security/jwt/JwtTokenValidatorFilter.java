package ge.atm.atmservice.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter checks Authorization header for all http requests
 * and if valid authentication header is present, sets the authentication principal to the security context.
 *
 * @see JwtTokenProvider#resolveToken(HttpServletRequest)
 * @see JwtTokenProvider#validateToken(String)
 * @see JwtTokenProvider#getAuthentication(String)
 */
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	private final JwtTokenProvider tokenProvider;

	public JwtTokenValidatorFilter(JwtTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String accessToken = tokenProvider.resolveToken(request);

		if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
			Authentication authentication = tokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
