package com.loctran.User;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getUsers() {
       return userDAO.getUsers();
    }

    public User getUsersByID(UUID id) {

        return getUsers().stream().filter(u -> u.getId().equals(id)).findFirst().
                orElseThrow(() -> new IllegalArgumentException("no user with id " + id));
    }
}
