package ge.atm.atmservice.security;

import ge.atm.atmservice.security.jwt.JwtTokenProvider;
import ge.atm.atmservice.security.jwt.JwtTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CardAuthenticationProvider authenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CardAuthenticationProvider authenticationProvider, JwtTokenProvider jwtTokenProvider) {
        this.authenticationProvider = authenticationProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/authentication/initiate").permitAll()
                .antMatchers("/api/authentication/finalize").authenticated()
                .antMatchers("/api/**").hasAuthority(Roles.ROLE_FINALIZED_AUTHENTICATION)
                .and()
                .addFilterBefore(jwtTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private AuthenticationEntryPoint unauthorizedHandler() {
        return new Http401UnauthorizedEntryPoint();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public JwtTokenValidatorFilter jwtTokenValidatorFilter() {
        return new JwtTokenValidatorFilter(jwtTokenProvider);
    }

}
