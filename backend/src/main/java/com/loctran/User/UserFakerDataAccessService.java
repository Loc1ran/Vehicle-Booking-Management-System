package com.loctran.User;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("faker")
public class UserFakerDataAccessService implements UserDAO{

    @Override
    public List<User> getUsers() {
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            users.add(new User(UUID.randomUUID(), faker.name().name(), "password"));
        }

        return users;

    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return getUsers().stream().filter(u -> u.getId().equals(id)).findFirst();
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