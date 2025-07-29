package com.loctran.User;

import com.loctran.AbstractDaoUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserJDBCDataAccessServiceTest extends AbstractDaoUnitTest {
    private UserJDBCDataAccessService underTest;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new UserJDBCDataAccessService(
                getJdbcTemplate(),
                userRowMapper
        );
    }

    @Test
    void getUsers() {
        User user = new User(UUID.randomUUID(), "Loc");

        underTest.saveUser(user);

        List<User> actual = underTest.getUsers();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void getUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId,"Loc");

        underTest.saveUser(user);

        Optional<User> actual = underTest.getUserById(userId);

        assertThat(actual).isPresent().hasValueSatisfying(u -> {
                assertThat(u.getId()).isEqualTo(userId);
                assertThat(u.getName()).isEqualTo("Loc");
        });
    }

    @Test
    void notReturnGetUserById(){
        UUID NEVER_USED_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

        var actual = underTest.getUserById(NEVER_USED_UUID);

        assertThat(actual).isEmpty();
    }

    @Test
    void saveUser() {
        User user = new User(UUID.randomUUID(), "Loc");

        underTest.saveUser(user);

        List<User> actual = underTest.getUsers();

        assertThat(actual).isNotEmpty();

    }

    @Test
    void deleteUser() {
        User user = new User(UUID.randomUUID(), "Loc");

        underTest.saveUser(user);
        underTest.deleteUser(user.getId());

        Optional<User> actual = underTest.getUserById(user.getId());

        assertThat(actual).isNotPresent();
    }

    @Test
    void updateUser() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Loc");

        underTest.saveUser(user);

        String newName = "Hoa";

        User newUser = new User();
        newUser.setId(userId);
        newUser.setName(newName);

        underTest.updateUser(newUser);

        Optional<User> actual = underTest.getUserById(userId);

        assertThat(actual).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getName()).isEqualTo(newName);
        });

    }
}