package com.loctran.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("userJDBC")
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
                FROM user_info
                LIMIT 1000;
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
        if (user.getId() != null) {
            var sql = """
                    INSERT INTO user_info(id, name, password)
                    VALUES (?, ?, ?);
                    """;
            jdbcTemplate.update(sql, user.getId(), user.getName(), user.getPassword());
        } else{
            var sql = """
                       INSERT INTO user_info(name, password) 
                       VALUES (?, ?)
                       """;
            jdbcTemplate.update(sql, user.getName(), user.getPassword());
        }
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

    @Override
    public Optional<User> findByName(String name) {
        var sql = """
                SELECT *
                FROM user_info
                WHERE name = ?;
                """;
        return jdbcTemplate.query(sql, userRowMapper, name).stream().findFirst();
    }
}
