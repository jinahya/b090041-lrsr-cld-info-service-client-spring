package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
final class ProjectProperties {

    private static final Properties PROPERTIES;

    private static final String NAME = "/project.properties";

    static {
        final Properties properties = new Properties();
        try {
            try (InputStream resource = ProjectProperties.class.getResourceAsStream(NAME)) {
                properties.load(resource);
            }
            PROPERTIES = properties;
            // assert filtered
            Paths.get(properties.getProperty("baseDir"));
        } catch (final IOException ioe) {
            log.error("failed to load properties from {}", NAME, ioe);
            throw new InstantiationError(ioe.getMessage());
        }
    }

    static Properties properties() {
        return new Properties(PROPERTIES);
    }

    static String baseDir() {
        return properties().getProperty("baseDir");
    }

    static Path baseDirAsPath() {
        return Paths.get(baseDir());
    }

    static Path baseDirAsPathNormalized() {
        return baseDirAsPath().normalize();
    }

    private ProjectProperties() {
        throw new AssertionError("instantiation is not allowed");
    }
}
