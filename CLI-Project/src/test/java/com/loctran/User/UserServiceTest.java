package com.loctran.User;

import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import org.aspectj.weaver.patterns.IVerificationRequired;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService underTest;
    @Mock
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userDAO);
    }

    @Test
    void getUsers() {
        User user = new User(UUID.randomUUID(), "Loc");

        when(userDAO.getUsers()).thenReturn(List.of(user));

        List<User> actual = underTest.getUsers();

        assertThat(actual).isEqualTo(List.of(user));
        verify(userDAO).getUsers();
    }

    @Test
    void getUsersByID() {
        UUID userId = UUID.randomUUID();
        User user = new User(UUID.randomUUID(), "Loc");

        when(userDAO.getUserById(userId)).thenReturn(Optional.of(user));

        User actual = underTest.getUsersByID(userId);

        assertThat(actual).isEqualTo(user);
        verify(userDAO).getUserById(userId);
    }

    @Test
    void willThrownAnExceptionWhenNoUsersByID() {
        UUID userId = UUID.randomUUID();

        when(userDAO.getUserById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getUsersByID(userId)).isInstanceOf(ResourceNotFound.class)
                .hasMessage("user not found");

    }

    @Test
    void deleteUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc");

        when(userDAO.getUserById(userId)).thenReturn(Optional.of(user));

        underTest.deleteUser(userId);

        verify(userDAO).deleteUser(userId);
        verify(userDAO).getUserById(userId);
    }

    @Test
    void willThrowAnExceptionWhenNoDeleteUser() {
        UUID userId = UUID.randomUUID();

        when(userDAO.getUserById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.deleteUser(userId)).isInstanceOf(ResourceNotFound.class)
                .hasMessage("user not found");
    }

    @Test
    void saveUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        underTest.saveUser(user);

        verify(userDAO).saveUser(userCaptor.capture());

        User captorValue = userCaptor.getValue();

        assertThat(captorValue.getId()).isEqualTo(userId);
        assertThat(captorValue.getName()).isEqualTo("Loc");

    }

    @Test
    void willThrowAnExceptionWhenTryToSaveUserMissingName() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, null);

        assertThatThrownBy(() -> underTest.saveUser(user)).isInstanceOf(RequestValidationException.class)
                .hasMessage("name is required");
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc");

        when(userDAO.getUserById(userId)).thenReturn(Optional.of(user));

        String newName = "Hoa";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newName);

        underTest.updateUser(userId, updateUserRequest);

        verify(userDAO).getUserById(userId);
        verify(userDAO).updateUser(user);
    }

    @Test
    void willThrownAnExceptionWhenNoUserToUpdate() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc");

        when(userDAO.getUserById(userId)).thenReturn(Optional.of(user));

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(null);

        assertThatThrownBy(() -> underTest.updateUser(userId, updateUserRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
    }

    @Test
    void willThrownAnExceptionWhenNoUpdateUser() {
        UUID userId = UUID.randomUUID();

        when(userDAO.getUserById(userId)).thenReturn(Optional.empty());

        String newName = "Hoa";
        UpdateUserRequest updateUserRequest = new UpdateUserRequest(newName);

        assertThatThrownBy(() -> underTest.updateUser(userId, updateUserRequest))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("user not found");
    }
}