package com.loctran.User;

import java.util.List;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        List<String> roles,
        String username
){
}
