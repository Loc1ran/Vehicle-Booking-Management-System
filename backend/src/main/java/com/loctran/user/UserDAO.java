package com.loctran.User;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDAO {
    List<User> getUsers();
    Optional<User> getUserById(UUID id);
    void saveUser(User user);
    void deleteUser(UUID id);
    void updateUser(User updatedUser);
    Optional<User> findByName(String name);
}
