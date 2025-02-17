package com.loctran.User;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User[] getUsers() {
       return userDAO.getUsers();
    }

    public User getUsersByID(UUID id){
        User[] users = getUsers();

        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new IllegalArgumentException("No User with id" + id);
    }
}
