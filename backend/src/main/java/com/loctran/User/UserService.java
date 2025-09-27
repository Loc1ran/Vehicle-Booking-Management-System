package com.loctran.User;

import com.loctran.Exception.DuplicateResourceException;
import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final UserDTOMapper userDTOMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(@Qualifier("userJPA") UserDAO userDAO, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getUsers() {
        return userDAO.getUsers().stream().map(userDTOMapper).collect(Collectors.toList());
    }

    public UserDTO getUsersByID(UUID id) {
        return userDAO.getUserById(id).map(userDTOMapper).orElseThrow(() -> new ResourceNotFound("user not found"));
    }

    public User getUserEntityByID(UUID id) {
        return userDAO.getUserById(id).orElseThrow(() -> new ResourceNotFound("user not found"));
    }

    public void deleteUser(UUID id) {
        if ( userDAO.getUserById(id).isEmpty() ) {
            throw new ResourceNotFound("user not found");
        }
        userDAO.deleteUser(id);
    }

    public void saveUser(UserRegistrationRequest userRegistrationRequest) {
        if( userRegistrationRequest.name() == null) {
            throw new RequestValidationException("name is required");
        }

        if( userDAO.findByName(userRegistrationRequest.name()).isPresent() ) {
            throw new DuplicateResourceException("name already exists");
        }

        User user = new User(userRegistrationRequest.name(),
                passwordEncoder.encode(userRegistrationRequest.password()));
        userDAO.saveUser(user);

    }

    public void updateUser(UUID id, UpdateUserRequest userRequest) {
        User user = userDAO.getUserById(id).orElseThrow(() -> new ResourceNotFound("user not found"));
        boolean changes = false;

        if(userDAO.findByName(userRequest.name()).isPresent()){
            throw new DuplicateResourceException("username already exists");
        }

        if ( userRequest.name() != null && !userRequest.name().equals(user.getName())) {
            user.setName(userRequest.name());
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("no data changes found");
        }

        userDAO.updateUser(user);
    }

    public User findByName(String name) {
        return userDAO.findByName(name).orElseThrow(() -> new ResourceNotFound("user not found"));
    }
}
