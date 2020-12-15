package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.LocalDate;

import static java.lang.String.format;

/**
 * A client implementation uses an instance of {@link RestTemplate}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see LrsrCldInfoServiceReactiveClient
 */
@Lazy
@Validated
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

    // -----------------------------------------------------------------------------------------------------------------
    public ResponseEntity<Response> getLunCalInfo(@NotNull final LocalDate solarDate) {
        final URI url = UriComponentsBuilder
                .fromUri(rootUri)
                .pathSegment("getLunCalInfo")
                .queryParam("ServiceKey", serviceKey())
                .queryParam("solYear", format("%1$04d", solarDate.getYear()))
                .queryParam("solMonth", format("%1$02d", solarDate.getMonthValue()))
                .queryParam("solDay", format("%1$02d", solarDate.getDayOfMonth()))
                .build()
                .toUri();
        return restTemplate.exchange(url, HttpMethod.GET, null, Response.class);
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
    @LrsrCldInfoServiceRestTemplateRootUri
    @Autowired(required = false)
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private String restTemplateRootUri;
}
