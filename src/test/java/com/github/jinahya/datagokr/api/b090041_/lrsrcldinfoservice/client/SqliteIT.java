package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Slf4j
abstract class SqliteIT extends LrsrCldInfoServiceClientIT {

    private static final Properties PROPERTIES = ProjectProperties.properties();

    private static final String BASE_DIR = ProjectProperties.baseDir().replace("\\", "/");

    private static final String URL = "jdbc:sqlite:" + BASE_DIR + "/out/calendar.db";

    static Connection connection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    static <R> R connection(final Function<? super Connection, ? extends R> function) throws SQLException {
        requireNonNull(function, "function is null");
        try (Connection connection = connection()) {
            return function.apply(connection);
        }
    }

    static void connection(final Consumer<? super Connection> consumer) throws SQLException {
        requireNonNull(consumer, "consumer is null");
        connection(c -> {
            consumer.accept(c);
            return null;
        });
    }

    protected SqliteIT() {
        super();
    }
}
