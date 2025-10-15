package com.loctran.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

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
        Page<User> page = mock(Page.class);
        List<User> users = List.of(new User());

        when(page.getContent()).thenReturn(users);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<User> expected = underTest.getUsers();

        assertThat(expected).isEqualTo(users);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(userRepository).findAll(pageableCaptor.capture());
        assertThat(pageableCaptor.getValue()).isEqualTo(Pageable.ofSize(1000));
    }

    @Test
    void getUserById() {
        UUID userId = UUID.randomUUID();

        underTest.getUserById(userId);

        verify(userRepository).findById(userId);
    }

    @Test
    void saveUser() {
        User user = new User(UUID.randomUUID(), "Loc", "password");

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
        User updateUser = new User(UUID.randomUUID(), "Hoa", "password");

        underTest.updateUser(updateUser);

        verify(userRepository).save(updateUser);
    }
}