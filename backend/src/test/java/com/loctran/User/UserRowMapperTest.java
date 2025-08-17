package com.loctran.User;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        UserRowMapper userRowMapper = new UserRowMapper();
        UUID userId = UUID.randomUUID();

        ResultSet rs = mock(ResultSet.class);
        when(rs.getObject("id", UUID.class)).thenReturn(userId);
        when(rs.getString("name")).thenReturn("Loc");

        User actual = userRowMapper.mapRow(rs, 1);

        User expected = new User(userId, "Loc");

        assertThat(actual).isEqualTo(expected);
    }
}