package com.loctran.auth;

import com.loctran.user.User;
import com.loctran.user.UserDTO;
import com.loctran.user.UserDTOMapper;
import com.loctran.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServices {

    private final AuthenticationManager authenticationManager;
    private final UserDTOMapper userDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthenticationServices(AuthenticationManager authenticationManager, UserDTOMapper userDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDTOMapper = userDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = (User) authentication.getPrincipal();
        UserDTO userDTO = userDTOMapper.apply(user);
        String jwtToken = jwtUtil.issueToken(userDTO.name(), userDTO.roles());

        return new AuthenticationResponse(
                jwtToken, userDTO
        );
    }
}
