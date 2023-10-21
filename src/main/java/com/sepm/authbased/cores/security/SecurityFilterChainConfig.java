package com.sepm.authbased.cores.security;

import com.sepm.authbased.cores.jwt.JwtRequestFilterHandler;
import jakarta.servlet.ServletException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.sepm.authbased.modules.user.Permission.*;
import static com.sepm.authbased.modules.user.Role.*;
import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@Configuration
public class SecurityFilterChainConfig {
    private final JwtRequestFilterHandler jwtRequestFilterHandler;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    public SecurityFilterChainConfig(JwtRequestFilterHandler jwtRequestFilterHandler,
                                     AuthenticationProvider authenticationProvider,
                                     LogoutHandler logoutHandler) {
        this.jwtRequestFilterHandler = jwtRequestFilterHandler;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests()

                .requestMatchers(POST,  "/api/v1/auth/register", "/api/v1/auth/login")
                .permitAll()
//                .requestMatchers("/api/v1/demo-controller")
//                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtRequestFilterHandler, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(((request, response, authentication) -> {
                                    try {
                                        request.logout();
                                    }catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    SecurityContextHolder.clearContext();

                                }))
                                .deleteCookies("token")
                                .invalidateHttpSession(true)
                )
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint());


        return http.build();
    }

    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint() {
        return new UnauthorizedEntryPoint();
    }
}