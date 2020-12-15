package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A component class for accessing {@link #BASE_URL}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see LrsrCldInfoServiceClient
 */
@Validated
@Lazy
@Component
@Slf4j
public class LrsrCldInfoServiceReactiveClient extends AbstractLrsrCldInfoServiceClient {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An injection qualifier for an instance of {@link WebClient} for accessing {@link #BASE_URL}.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceWebClient {

    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    @LrsrCldInfoServiceWebClient
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private WebClient webClient;
}
