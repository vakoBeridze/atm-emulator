package ge.atm.atmservice.security.jwt;


import ge.atm.bankservice.domain.dto.CardDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class JwtCardDetails implements UserDetails {

	private final String cardNumber;
	private final CardDto.PreferredAuthEnum preferredAuthenticationMethod;
	private final Collection<? extends GrantedAuthority> authorities;


	public JwtCardDetails(String cardNumber, CardDto.PreferredAuthEnum authenticationMethod, Collection<? extends GrantedAuthority> authorities) {
		this.cardNumber = cardNumber;
		this.preferredAuthenticationMethod = authenticationMethod;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return cardNumber;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public Collection<String> getRoles() {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public CardDto.PreferredAuthEnum getPreferredAuthenticationMethod() {
		return preferredAuthenticationMethod;
	}
}
