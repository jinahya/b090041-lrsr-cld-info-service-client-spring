package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
     * The path segment value for {@code /api}. The value is {@value}.
     */
    protected static final String PATH_SEGMENT_API = "api";

    /**
     * The path value for {@code /api}. The value is {@value}.
     */
    protected static final String PATH_API = '/' + PATH_SEGMENT_API;

    /**
     * The query parameter name for {@code ?key}. The value is {@value}.
     */
    protected static final String QUERY_PARAM_KEY = "key";

    /**
     * The authorization header type for token. The value is {@value}.
     */
    protected static final String AUTHORIZATION_TYPE_TOKEN = "Token";

    /**
     * Returns a value for {@link HttpHeaders#AUTHORIZATION} header for specified token value.
     *
     * @param token the token value.
     * @return a value for {@link HttpHeaders#AUTHORIZATION} header for specified token value.
     */
    protected static String authorizationHeaderValueForToken(final String token) {
        Assert.isTrue(StringUtils.hasText(token), "token has no text");
        return AUTHORIZATION_TYPE_TOKEN + ' ' + token;
    }
}
