package ru.alfastrah.edu.restsecurityserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.HttpMethod.*;

@Configuration
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER = "USER";
    private static final String SUPERUSER = "SUPERUSER";

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .passwordEncoder(encoder)
                .withUser("user").password(encoder.encode("password")).roles(USER)
                .and().withUser("superuser").password(encoder.encode("password")).roles(SUPERUSER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .authorizeRequests()
                .antMatchers(POST, "/contract").hasRole(SUPERUSER)
                .antMatchers(DELETE, "/contract/*").hasRole(SUPERUSER)
                .antMatchers(GET, "/contract").hasAnyRole(SUPERUSER, USER)
                .antMatchers(GET, "/contract/*").hasAnyRole(SUPERUSER, USER)
                .antMatchers("/**").denyAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
    }
}
