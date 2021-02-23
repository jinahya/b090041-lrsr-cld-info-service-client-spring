package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class Sqlite_Ganzhi_IT extends SqliteIT {

    private static final String TABLE = "ganzhi";

    static final int STEMS_LENGTH = 10;

    static final String STEMS_KORE = "갑을병정무기경신임계";

    static final String STEMS_HANS = "甲乙丙丁戊己庚辛壬癸";

    static final int BRANCHES_LENGTH = 12;

    static final String BRANCHES_KORE = "자축인묘진사오미신유술해";

    static final String BRANCHES_HANS = "子丑寅卯辰巳午未申酉戌亥";

    static Long selectGanzhiId(final Connection connection, final String kore, final String hans) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id FROM " + TABLE + " WHERE kore = ? AND hans = ?")) {
            int index = 0;
            statement.setString(++index, kore);
            statement.setString(++index, hans);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return resultSet.getLong("id");
            }
        }
    }

    static int insert(final Connection connection, final String kore, final String hans) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + TABLE + "(kore, hans) VALUES (?, ?)")) {
            int index = 0;
            statement.setString(++index, kore);
            statement.setString(++index, hans);
            return statement.executeUpdate();
        }
    }

    @Disabled
    @Test
    void insert() throws SQLException {
        try (Connection connection = connection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + TABLE + "(kore, hans) VALUES (?, ?)")) {
                int s = 0;
                int b = 0;
                for (int i = 0; i < 60; i++) {
                    final String kore = STEMS_KORE.charAt(s) + "" + BRANCHES_KORE.charAt(b);
                    final String hans = STEMS_HANS.charAt(s) + "" + BRANCHES_HANS.charAt(b);
                    log.debug("{}: kore: {}, hans: {}", String.format("%1$02d", i), kore, hans);
                    statement.clearParameters();
                    int index = 0;
                    statement.setString(++index, kore);
                    statement.setString(++index, hans);
                    final int inserted = statement.executeUpdate();
                    assertThat(inserted).isOne();
                    s = (++s) % STEMS_LENGTH;
                    b = (++b) % BRANCHES_LENGTH;
                }
            }
        }
    }
}
