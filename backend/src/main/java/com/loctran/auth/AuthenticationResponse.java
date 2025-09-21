package com.loctran.auth;

import com.loctran.User.UserDTO;

public record AuthenticationResponse (
        String jwtToken,
        UserDTO userDTO
){
}
