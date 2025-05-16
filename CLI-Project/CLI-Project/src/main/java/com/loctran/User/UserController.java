package com.loctran.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable("userId") UUID userId) {
        return userService.getUsersByID(userId);
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.saveUser(user);
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
