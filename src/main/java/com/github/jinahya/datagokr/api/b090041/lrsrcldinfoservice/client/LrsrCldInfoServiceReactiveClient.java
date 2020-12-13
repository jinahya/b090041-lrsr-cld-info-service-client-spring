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
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.util.Objects.requireNonNull;

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

    /**
     * Creates a new instance with specified web client.
     *
     * @param webClient the web client.
     * @return a new instance with {@code webClient}.
     */
    static LrsrCldInfoServiceReactiveClient of(final WebClient webClient) {
        requireNonNull(webClient, "webClient is null");
        final LrsrCldInfoServiceReactiveClient instance = new LrsrCldInfoServiceReactiveClient();
        instance.webClient = webClient;
        return instance;
    }

//    // -----------------------------------------------------------------------------------------------------------------
//
//    /**
//     * Reads all entries associated with specified token.
//     *
//     * @param token an authorization token.
//     * @return a mono of message.
//     */
//    @NotNull
//    public Mono<Message> readAll(@NotBlank final String token) {
//        return webClient()
//                .get()
//                .uri(b -> b.pathSegment(PATH_SEGMENT_API).build())
//                .header(HttpHeaders.AUTHORIZATION, authorizationHeaderValueForToken(token))
//                .retrieve()
//                .bodyToMono(Message.class);
//    }
//
//    /**
//     * Reads entries which each is associated to specified keys.
//     *
//     * @param token an authorization token.
//     * @param keys  the keys of which the entries are retrieved.
//     * @return a mono of message.
//     */
//    @NotNull
//    public Mono<Message> readFor(@NotBlank final String token, final String... keys) {
//        requireNonNull(keys, "keys is null");
//        final String varval = stream(keys).map(String::trim).filter(v -> !v.isEmpty()).collect(joining(","));
//        Assert.isTrue(StringUtils.hasText(varval), "keys are empty");
//        return webClient()
//                .get()
//                .uri(b -> b.pathSegment(PATH_SEGMENT_API).queryParam(QUERY_PARAM_KEY, "{key}").build(varval))
//                .header(HttpHeaders.AUTHORIZATION, authorizationHeaderValueForToken(token))
//                .retrieve()
//                .bodyToMono(Message.class);
//    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    @LrsrCldInfoServiceWebClient
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private WebClient webClient;
}
