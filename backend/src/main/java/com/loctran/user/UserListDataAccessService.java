package com.loctran.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("userList")
public class UserListDataAccessService implements UserDAO {
    private static final List<User> users;
    static {
        users = new ArrayList<>();
        users.add(new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "James", "password"));
        users.add(new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "Jamila", "password"));

    }
    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void deleteUser(UUID id) {

    }

    @Override
    public void updateUser(User updatedUser) {

    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.empty();
    }


}