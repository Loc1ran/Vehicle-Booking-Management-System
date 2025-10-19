package com.loctran.auth;

import com.loctran.user.UserDTO;

public record AuthenticationResponse (
        String jwtToken,
        UserDTO userDTO
){
}
