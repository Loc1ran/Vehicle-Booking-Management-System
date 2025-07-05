package com.loctran.User;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserJDBCDataAccessService implements UserDAO {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserJDBCDataAccessService(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }
    @Override
    public List<User> getUsers() {
        var sql = """
                SELECT *
                FROM user_info;
                """;
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        var sql = """
                SELECT *
                FROM user_info
                WHERE id = ?;
                """;
        return jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst();
    }

    @Override
    public void saveUser(User user) {
        var sql = """
                INSERT INTO user_info(id, name)
                VALUES (?, ?);
                """;
        jdbcTemplate.update(sql, user.getId(), user.getName());
    }

    @Override
    public void deleteUser(UUID id) {
        var sql = """
                DELETE FROM user_info
                WHERE id = ?;
                """;
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateUser(User updatedUser) {
        if ( updatedUser.getName() != null ) {
            var sql = """
                    UPDATE user_info SET name = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, updatedUser.getName(), updatedUser.getId());
        }
    }
}
