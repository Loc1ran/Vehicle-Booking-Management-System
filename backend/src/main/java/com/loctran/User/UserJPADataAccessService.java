package com.loctran.User;


import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("userJPA")
public class UserJPADataAccessService implements UserDAO {
    private final UserRepository userRepository;

    public UserJPADataAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User updatedUser) {
        userRepository.save(updatedUser);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

}
