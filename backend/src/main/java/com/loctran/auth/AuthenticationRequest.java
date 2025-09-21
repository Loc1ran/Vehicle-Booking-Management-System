package com.loctran.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
