package com.jazy.auth;

import com.jazy.customer.CustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationResponse.class);

    @Value("${jwt.cookieExpiry}")
    private int cookieExpiry;

    private final AuthenticationService authorizationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authorizationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        AuthenticationResponse response =
                authorizationService.login(authenticationRequest);

        ResponseCookie cookie = ResponseCookie.from("accessToken", response.token())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response.customerDto());
    }

    @PostMapping("logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = authorizationService.logout();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been logout");
    }
}
