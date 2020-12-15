package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

abstract class AbstractLrsrCldInfoServiceClientIT<T extends AbstractLrsrCldInfoServiceClient> {

    static final String SYSTEM_PROPERTY_SERVICE_KEY = "serviceKey";

    // -----------------------------------------------------------------------------------------------------------------
    @Configuration
    static class _Configuration {

        @AbstractLrsrCldInfoServiceClient.LrsrCldInfoServiceServiceKey
        @Bean
        String lrsrCldInfoServiceServiceKey() {
            return System.getProperty(SYSTEM_PROPERTY_SERVICE_KEY);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance with specified client class.
     *
     * @param clientClass the client class to test.
     * @see #clientClass
     */
    AbstractLrsrCldInfoServiceClientIT(final Class<T> clientClass) {
        super();
        this.clientClass = requireNonNull(clientClass, "clientClass is null");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The client class to test.
     */
    final Class<T> clientClass;

    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private T clientInstance;
}