package com.loctran.User;

import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class UserJPADataAccessServiceTest {
    private UserJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserJPADataAccessService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getUsers() {
        underTest.getUsers();

        verify(userRepository).findAll();
    }

    @Test
    void getUserById() {
        UUID userId = UUID.randomUUID();

        underTest.getUserById(userId);

        verify(userRepository).findById(userId);
    }

    @Test
    void saveUser() {
        User user = new User(UUID.randomUUID(), "Loc");

        underTest.saveUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();

        underTest.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void updateUser() {
        User updateUser = new User(UUID.randomUUID(), "Hoa");

        underTest.updateUser(updateUser);

        verify(userRepository).save(updateUser);
    }
}