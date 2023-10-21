package com.sepm.authbased.cores.auth;

import com.sepm.authbased.cores.jwt.JwtUtil;
import com.sepm.authbased.cores.token.Token;
import com.sepm.authbased.cores.token.TokenRepository;
import com.sepm.authbased.cores.token.TokenType;
import com.sepm.authbased.exception.ResourceNotFoundException;
import com.sepm.authbased.modules.user.User;
import com.sepm.authbased.modules.user.UserDTO;
import com.sepm.authbased.modules.user.UserDTOMapper;
import com.sepm.authbased.modules.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    // user and token repo, pw encoder, jwt util, auth manager
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDTOMapper userDTOMapper;

    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        // Build User obj
        // Save user
        // Generate token and refresh token
        // save to auth response
        var userObj = User.builder()
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .email(registrationRequest.email())
                .password(passwordEncoder.encode((registrationRequest.password())))
                .role(registrationRequest.role())
                .build();

        var savedUser = userRepository.save(userObj);
        var accessToken = jwtUtil.generateToken(userObj);
        var refreshToken = jwtUtil.generateRefreshToken(userObj);

        System.out.println("Token = " + accessToken);
        System.out.println("Refresh Token = " + refreshToken);
        saveUserToken(savedUser, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.email(),
                authenticationRequest.password()
        ));

        var user = userRepository.findUserByEmail(authenticationRequest.email())
                .orElseThrow();

        var accessToken = jwtUtil.generateToken(user);
        var refreshToken = jwtUtil.generateRefreshToken(user);

        this.revokeAllUserTokens(user);
        this.saveUserToken(user, accessToken);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserDTO getLoggedInUser(Authentication authentication) {
        UserDetails userDetails = (User)authentication.getPrincipal();
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userDTOMapper.apply(user);
    }

    private void saveUserToken(User user, String token) {
        var tokenObj = Token.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .user(user)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(tokenObj);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> tokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if(tokens.isEmpty()) {
            return;
        }

        tokens.forEach(token -> { token.setExpired(true); token.setRevoked(true); });
        tokenRepository.saveAll(tokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

    }
}
