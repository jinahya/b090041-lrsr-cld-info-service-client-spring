package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An abstract parent class for client classes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public abstract class AbstractLrsrCldInfoServiceClient {

    public static final String BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService";

    public static final String BASE_URL_DEVELOPMENT = BASE_URL;

    public static final String BASE_URL_PRODUCTION = BASE_URL_DEVELOPMENT;

    /**
     * An injection qualifier for the {@code ServiceKey} parameter.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceServiceKey {

    }

    protected static final String QUERY_PARAM_NAME_SERVICE_KEY = "ServiceKey";

    @LrsrCldInfoServiceServiceKey
    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(value = AccessLevel.PROTECTED)
    private String serviceKey;
}
