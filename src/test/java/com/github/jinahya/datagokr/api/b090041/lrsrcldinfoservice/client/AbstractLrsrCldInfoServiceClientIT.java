package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static java.util.Objects.requireNonNull;

/**
 * An abstract base class for testing subclasses of {@link AbstractLrsrCldInfoServiceClient} class.
 *
 * @param <T> subclass type parameter
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
abstract class AbstractLrsrCldInfoServiceClientIT<T extends AbstractLrsrCldInfoServiceClient> {

    static final String SYSTEM_PROPERTY_URL = "url";

    static final String SYSTEM_PROPERTY_TOKEN = "token";

    static final String SYSTEM_PROPERTY_KEYS = "keys";

    // -----------------------------------------------------------------------------------------------------------------
    @Configuration
    static class _Configuration {

        @Bean
        ObjectMapper objectMapper() {
            return new Jackson2ObjectMapperBuilder().build();
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

    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private ObjectMapper objectMapper;
}