package com.sepm.authbased.cores.auth;

import com.sepm.authbased.cores.jwt.JwtUtil;
import com.sepm.authbased.modules.user.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
        jwtUtil.storeTokenToCookie(response, authenticationResponse.getAccessToken());
        return ResponseEntity.ok(authenticationResponse);
    }


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(Authentication authentication) {
        UserDTO userDTO = authenticationService.getLoggedInUser(authentication);

        return ResponseEntity.ok(userDTO);
    }
}
