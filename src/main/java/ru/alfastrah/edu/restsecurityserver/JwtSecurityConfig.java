package ru.alfastrah.edu.restsecurityserver;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static ru.alfastrah.edu.restsecurityserver.ContractController.CONTRACT_BASE_URL;

@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER = "USER";
    private static final String SUPERUSER = "SUPERUSER";
    private static final String ADMIN = "ADMIN";

    @Setter(onMethod = @__({@Autowired}))
    private JwtUserDetailsService userDetailsService;
    @Setter(onMethod = @__({@Autowired}))
    PasswordEncoder passwordEncoder;
    @Setter(onMethod = @__({@Autowired}))
    JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers(POST, CONTRACT_BASE_URL).hasRole(SUPERUSER)
                .mvcMatchers(DELETE, CONTRACT_BASE_URL).hasRole(SUPERUSER)
                .mvcMatchers(GET, CONTRACT_BASE_URL).hasAnyRole(SUPERUSER, USER)
                .mvcMatchers("/user").hasRole(ADMIN)
                .mvcMatchers("/").denyAll()
                .and().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
