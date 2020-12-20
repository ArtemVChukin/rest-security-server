package ru.alfastrah.edu.restsecurityserver;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.GET;
import static ru.alfastrah.edu.restsecurityserver.ContractController.CONTRACT_BASE_URL;

@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER = "USER";
    private static final String SUPERUSER = "SUPERUSER";

    @Setter(onMethod = @__({@Autowired}))
    private CustomUserDetailsService userDetailsService;
    @Setter(onMethod = @__({@Autowired}))
    private JwtService jwtService;
    @Setter(onMethod = @__({@Autowired}))
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(CONTRACT_BASE_URL).hasRole(SUPERUSER)
                .mvcMatchers(GET, CONTRACT_BASE_URL).hasAnyRole(USER, SUPERUSER)
                .mvcMatchers("/user").permitAll()
                .mvcMatchers("/").denyAll()
                .and().csrf().disable()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService::parseJwtToken))
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService::createJwtToken))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
