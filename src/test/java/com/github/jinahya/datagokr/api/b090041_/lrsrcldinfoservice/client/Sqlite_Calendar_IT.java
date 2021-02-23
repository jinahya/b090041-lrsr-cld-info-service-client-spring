package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.SqliteIT.connection;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.Sqlite_Ganzhi_IT.selectGanzhiId;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class Sqlite_Calendar_IT extends LrsrCldInfoServiceClientIT {

    private static final String TABLE = "calendar";

    //    private static final YearMonth LOWER = YearMonth.of(1391, Month.FEBRUARY);
    private static final YearMonth LOWER = YearMonth.of(1900, Month.FEBRUARY);

    private static final YearMonth UPPER = YearMonth.of(2050, Month.DECEMBER);

    private static long countForSolarYearAndSolarMonth(final YearMonth solarYearMonth) throws SQLException {
        requireNonNull(solarYearMonth, "solarYearMonth is null");
        try (Connection c = connection()) {
            try (PreparedStatement statement = c.prepareStatement(
                    "SELECT COUNT(solar_day_of_month) FROM " + TABLE + " WHERE solar_year = ? AND solar_month = ?")) {
                statement.setInt(1, solarYearMonth.getYear());
                statement.setInt(2, solarYearMonth.getMonthValue());
                try (ResultSet resultSet = statement.executeQuery()) {
                    final boolean next = resultSet.next();
                    assert next;
                    return resultSet.getLong(1);
                }
            }
        }
    }

    private static int insert(final Connection connection, final Item item) throws SQLException {
        requireNonNull(connection, "connection is null");
        requireNonNull(item, "item is null");
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + TABLE + "(solar_year, solar_month, solar_day_of_month"
                + ", lunar_year, lunar_month, lunar_leap_month, lunar_day_of_month"
                + ", ganzhi_year_id, ganzhi_month_id, ganzhi_day_id"
                + ") VALUES ("
                + "?, ?, ?"
                + ", ?, ?, ?, ?"
                + ", ?, ?, ?)")) {
            int index = 0;
            final Long ganzhiYearId = selectGanzhiId(connection, item.getGanzhiYearKore(), item.getGanzhiYearHans());
            assertThat(ganzhiYearId).isNotNull();
            final Long ganzhiMonthId
                    = item.getLunLeapmonth()
                      ? null : selectGanzhiId(connection, item.getGanzhiMonthKore(), item.getGanzhiMonthHans());
            assertThat(item.getLunLeapmonth() || ganzhiMonthId != null).isTrue();
            final Long ganzhiDayId = selectGanzhiId(connection, item.getGanzhiDayKore(), item.getGanzhiDayHans());
            assertThat(ganzhiDayId).isNotNull();
            statement.setInt(++index, item.getSolYear().getValue());
            statement.setInt(++index, item.getSolMonth().getValue());
            statement.setInt(++index, item.getSolDay());
            statement.setInt(++index, item.getLunYear().getValue());
            statement.setInt(++index, item.getLunMonth().getValue());
            statement.setInt(++index, item.getLunLeapmonth() ? 1 : 0);
            statement.setInt(++index, item.getLunDay());
            statement.setLong(++index, ganzhiYearId);
            if (ganzhiMonthId == null) {
                statement.setNull(++index, Types.NUMERIC);
            } else {
                statement.setLong(++index, ganzhiMonthId);
            }
            statement.setLong(++index, ganzhiDayId);
            return statement.executeUpdate();
        }
    }

    private static <R> R selectBySolar(final int solarYear, final int solarMonth, final int solarDayOfMonth,
                                       final Function<? super ResultSet, ? extends R> function)
            throws SQLException {
        try (Connection c = connection()) {
            try (PreparedStatement statement = c.prepareStatement(
                    "SELECT *"
                    + " FROM " + TABLE
                    + " WHERE solar_year = ? AND solar_month = ? AND solar_day_of_month = ?")) {
                int index = 0;
                statement.setInt(++index, solarYear);
                statement.setInt(++index, solarMonth);
                statement.setInt(++index, solarDayOfMonth);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return function.apply(resultSet);
                }
            }
        }
    }

    private static boolean existsBySolar(final int solarYear, final int solarMonth, final int solarDayOfMonth)
            throws SQLException {
        return selectBySolar(solarYear, solarMonth, solarDayOfMonth, r -> {
            try {
                return r.next();
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    private static boolean existsBySolar(final Item item) throws SQLException {
        return existsBySolar(item.getSolYear().getValue(), item.getSolMonth().getValue(), item.getSolDay());
    }

    private static <R> R selectByLunar(final int lunarYear, final int lunarMonth, final boolean lunarLeapMonth,
                                       final int lunarDayOfMonth,
                                       final Function<? super ResultSet, ? extends R> function)
            throws SQLException {
        try (Connection c = connection()) {
            try (PreparedStatement statement = c.prepareStatement(
                    "SELECT *"
                    + " FROM " + TABLE
                    + " WHERE lunar_year = ? AND lunar_month = ? AND lunar_leap_month = ?"
                    + " AND lunar_day_of_month = ?")) {
                int index = 0;
                statement.setInt(++index, lunarYear);
                statement.setInt(++index, lunarMonth);
                statement.setInt(++index, lunarLeapMonth ? 1 : 0);
                statement.setInt(++index, lunarDayOfMonth);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return function.apply(resultSet);
                }
            }
        }
    }

    private static boolean existsByLunar(final int lunarYear, final int lunarMonth, final boolean lunarLeapMonth,
                                         final int lunarDayOfMonth)
            throws SQLException {
        return selectByLunar(lunarYear, lunarMonth, lunarLeapMonth, lunarDayOfMonth, r -> {
            try {
                return r.next();
            } catch (final SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        });
    }

    private static boolean existsByLunar(final Item item) throws SQLException {
        return existsByLunar(item.getLunYear().getValue(), item.getLunMonth().getValue(), item.getLunLeapmonth(),
                             item.getLunDay());
    }

    @Disabled
    @Test
    void update() throws SQLException {
        for (YearMonth m = LOWER; m.compareTo(UPPER) <= 0; m = m.plusMonths(1L)) {
            if (countForSolarYearAndSolarMonth(m) > 0L) {
                continue;
            }
            log.debug("m: {}", m);
            final List<Item> items = clientInstance().getLunCalInfo(Year.from(m), m.getMonth(), null);
            try (Connection connection = connection()) {
                connection.setAutoCommit(false);
                try {
                    for (final Item item : items) {
                        try {
                            final int inserted = insert(connection, item);
                            assertThat(inserted).isEqualTo(1);
                        } catch (final SQLException sqle) {
                            log.error("failed to update for " + item);
                            if (existsBySolar(item)) {
                                log.warn("duplicate by solar: {}", item);
                                continue;
                            }
                            if (existsByLunar(item)) {
                                log.warn("duplicate by lunar: {}", item);
                                continue;
                            }
                            throw sqle;
                        }
                    }
                    connection.commit();
                } catch (final SQLException sqle) {
                    connection.rollback();
                    throw sqle;
                }
            }
        }
    }
}
