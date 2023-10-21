package com.sepm.authbased.cores.security;

import com.sepm.authbased.cores.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogOutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwt;

        if(authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authenticationHeader.substring(7);
        // Find the token
        var token = tokenRepository.findByToken(jwt).orElse(null);

        System.out.println(token);

        System.out.println("Hahahahahahahahahahahah");

        if(token != null) {
            token.setRevoked(true);
            token.setExpired(true);
            tokenRepository.save(token);
            SecurityContextHolder.clearContext();
        }
    }
}
