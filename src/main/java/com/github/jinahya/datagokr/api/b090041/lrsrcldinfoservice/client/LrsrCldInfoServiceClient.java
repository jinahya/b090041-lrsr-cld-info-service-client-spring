package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;

/**
 * A client implementation uses an instance of {@link RestTemplate}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see LrsrCldInfoServiceReactiveClient
 * @deprecated Use {@link LrsrCldInfoServiceReactiveClient} if applicable.
 */
@Deprecated
@Validated
@Lazy
@Component
@Slf4j
public class LrsrCldInfoServiceClient extends AbstractLrsrCldInfoServiceClient {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An injection qualifier for an instance of {@link RestTemplate} for accessing {@link #BASE_URL}.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceRestTemplate {

    }

    /**
     * An injection qualifier for injecting a custom {@code root-uri} in non-spring-boot environments.
     *
     * @deprecated Just for non-spring-boot environments.
     */
    @Deprecated
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceRestTemplateRootUri {

    }

    // -----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    private void onPostConstruct() {
        rootUri = restTemplate.getUriTemplateHandler().expand("/");
        if (restTemplateRootUri != null) {
            log.info("custom root uri specified: {}", restTemplateRootUri);
            rootUri = URI.create(restTemplateRootUri);
        }
    }

//    // -----------------------------------------------------------------------------------------------------------------
//    private Message exchangeGetMessage(final URI url, final String token) {
//        final RequestEntity<?> requestEntity = RequestEntity
//                .get(url)
//                .header(HttpHeaders.AUTHORIZATION, authorizationHeaderValueForToken(token))
//                .build();
//        final ResponseEntity<Message> response = restTemplate().exchange(requestEntity, Message.class);
//        {
//            final HttpStatus status = response.getStatusCode();
//            log.debug("status: {}", status);
//            if (!status.is2xxSuccessful()) {
//                throw new RestClientException("unsuccessful status received: " + status);
//            }
//            if (status != HttpStatus.OK) {
//                throw new RestClientException("status is not ok: " + status);
//            }
//        }
//        return requireNonNull(response.getBody());
//    }
//
//    /**
//     * Reads all entries associated with specified token.
//     *
//     * @param token an authorization token.
//     * @return a retrieved message.
//     */
//    @NotNull
//    public Message readAll(@NotBlank final String token) {
//        final URI url = uriBuilderFromRootUriPathApi().build().toUri();
//        return exchangeGetMessage(url, token);
//    }
//
//    /**
//     * Reads entries associated to specified keys.
//     *
//     * @param token an authorization token.
//     * @param keys  the keys of which the entries are retrieved.
//     * @return a retrieved message.
//     */
//    @NotNull
//    public Message readFor(@NotBlank final String token, final String... keys) {
//        requireNonNull(keys, "keys is null");
//        final String varval = stream(keys).map(String::trim).filter(v -> !v.isEmpty()).collect(joining(","));
//        Assert.isTrue(StringUtils.hasText(varval), "keys are empty");
//        final URI url = uriBuilderFromRootUriPathApi()
//                .queryParam(QUERY_PARAM_KEY, "{key}")
//                .buildAndExpand(varval)
//                .toUri();
//        return exchangeGetMessage(url, token);
//    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a uri builder created from {@code rootUri}.
     *
     * @return a uri builder created from {@code rootUri}.
     */
    protected UriComponentsBuilder uriBuilderFromRootUri() {
        return UriComponentsBuilder.fromUri(rootUri);
    }

    /**
     * Returns a uri builder created from {@code rootUri} and {@link #PATH_SEGMENT_API} path segment appended.
     *
     * @return a uri builder of {@code rootUri/api}.
     */
    protected UriComponentsBuilder uriBuilderFromRootUriPathApi() {
        return uriBuilderFromRootUri().pathSegment(PATH_SEGMENT_API);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @LrsrCldInfoServiceRestTemplate
    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private RestTemplate restTemplate;

    /**
     * The root uri expanded with {@code '/'} from {@code restTemplate.uriTemplateHandler}.
     */
    private URI rootUri;

    /**
     * A custom root uri value for non-spring-boot environments.
     */
    @Deprecated
    @LrsrCldInfoServiceRestTemplateRootUri
    @Autowired(required = false)
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    String restTemplateRootUri;
}
