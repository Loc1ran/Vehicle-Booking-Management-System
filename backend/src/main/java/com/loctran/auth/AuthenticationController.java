package com.loctran.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationServices authenticationServices;

    public AuthenticationController(AuthenticationServices authenticationServices) {
        this.authenticationServices = authenticationServices;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationServices.login(request);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, response.jwtToken())
                .body(response);
    }
}
