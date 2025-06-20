package com.loctran.User;

import com.loctran.Exception.DuplicateResourceException;
import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserDAO userDAO;

    public UserService(@Qualifier("userJPA") UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    public User getUsersByID(UUID id) {

        return userDAO.getUserById(id).orElseThrow(() -> new ResourceNotFound("user not found"));
    }

    public void deleteUser(UUID id) {
        userDAO.deleteUser(id);
    }

    public void saveUser(User user) {
        if( user.getName() == null){
            throw new RequestValidationException("name is required");
        }
        userDAO.saveUser(user);
    }

    public void updateUser(UUID id, UpdateUserRequest userRequest) {
        User user = userDAO.getUserById(id).orElseThrow(() -> new ResourceNotFound("user not found"));
        boolean changes = false;


        if ( userRequest.name() != null && !userRequest.name().equals(user.getName())) {
            user.setName(userRequest.name());
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        userDAO.updateUser(user);
    }
}
