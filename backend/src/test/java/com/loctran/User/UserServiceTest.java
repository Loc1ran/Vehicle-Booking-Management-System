package com.loctran.User;

import com.loctran.Exception.RequestValidationException;
import com.loctran.Exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService underTest;
    @Mock
    private UserDAO userDAO;

    private UserDTOMapper userDTOMapper = new UserDTOMapper();
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userDAO, userDTOMapper, passwordEncoder);
    }

    @Test
    void getUsers() {
        User user = new User("Loc", "password");
        List<User> allUser = List.of(user);

        when(userDAO.getUsers()).thenReturn(allUser);

        List<UserDTO> expected = allUser.stream().map(userDTOMapper).toList();

        List<UserDTO> actual = underTest.getUsers();

        assertThat(actual).isEqualTo(expected);
        verify(userDAO).getUsers();
    }

    @Test
    void getUsersByID() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc", "password");

        when(userDAO.getUserById(userId)).thenReturn(Optional.of(user));

        UserDTO expected = userDTOMapper.apply(user);

        UserDTO actual = underTest.getUsersByID(userId);

        assertThat(actual).isEqualTo(expected);
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
        User user = new User(userId, "Loc", "password");

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
        UserRegistrationRequest request = new UserRegistrationRequest("Loc", "password");
        String passwordHash = "!@$@!$!@$@!$$@!#$89218347124";

        when(passwordEncoder.encode(request.password())).thenReturn("!@$@!$!@$@!$$@!#$89218347124");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        underTest.saveUser(request);

        verify(userDAO).saveUser(userCaptor.capture());

        User captorValue = userCaptor.getValue();

        assertThat(captorValue.getName()).isEqualTo("Loc");
        assertThat(captorValue.getPassword()).isEqualTo(passwordHash);

    }

    @Test
    void willThrowAnExceptionWhenTryToSaveUserMissingName() {
        UUID userId = UUID.randomUUID();
        UserRegistrationRequest request = new UserRegistrationRequest(null, "password");

        assertThatThrownBy(() -> underTest.saveUser(request)).isInstanceOf(RequestValidationException.class)
                .hasMessage("name is required");
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc", "password");

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
        User user = new User(userId, "Loc", "password");

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