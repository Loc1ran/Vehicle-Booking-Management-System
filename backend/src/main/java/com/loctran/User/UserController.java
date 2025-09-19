package com.loctran.User;

import com.loctran.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public UserController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{userId}")
    public UserDTO getUserById(@PathVariable("userId") UUID userId) {
        return userService.getUsersByID(userId);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest request) {
        userService.saveUser(request);

        User savedUser = userService.findByName(request.name());

        String jwtToken = jwtUtil.issueToken(String.valueOf(savedUser.getId()), "ROLE_USER");

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();

    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") UUID id) {
        userService.deleteUser(id);
    }

    @PutMapping("{userId}")
    public void updateUser(@PathVariable("userId") UUID id, @RequestBody UpdateUserRequest updateRequest) {
        userService.updateUser(id, updateRequest);
    }

}
