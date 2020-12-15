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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.function.Consumer;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response.Body;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

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
    @NotNull
    public ResponseEntity<Response> getLunCalInfo(@NotNull final Year year, @NotNull final Month month,
                                                  @Nullable final Integer dayOfMonth, @Positive final Integer pageNo) {
        final URI url = UriComponentsBuilder
                .fromUri(rootUri)
                .pathSegment("getLunCalInfo")
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam("solYear", Response.Body.Item.YEAR_FORMATTER.format(year))
                .queryParam("solMonth", Response.Body.Item.MONTH_FORMATTER.format(month))
                .queryParamIfPresent("solDay", ofNullable(dayOfMonth)
                        .map(v -> MonthDay.of(month, v))
                        .map(Response.Body.Item.DAY_OF_MONTH_FORMATTER::format))
                .queryParamIfPresent("pageNo", ofNullable(pageNo))
                .build()
                .toUri();
        return restTemplate.exchange(url, HttpMethod.GET, null, Response.class);
    }

    protected List<Response.Body.@NotNull @Valid Item> getItems(@NotNull final ResponseEntity<Response> entity) {
        requireNonNull(entity, "entity is null");
        final Response response = entity.getBody();
        if (response == null) {
            throw new RuntimeException("null body from the response entity");
        }
        return getItems(response);
    }

    @Valid
    @NotNull
    public Body.Item getLunCalInfo(@NotNull final LocalDate localDate) {
        final ResponseEntity<Response> entity = getLunCalInfo(
                Year.of(localDate.getYear()), localDate.getMonth(), localDate.getDayOfMonth(), null);
        final List<Body.Item> items = getItems(entity);
        if (items.isEmpty()) {
            throw new RuntimeException("no items in response");
        }
        assert items.size() == 1;
        return items.get(0);
    }

    public void getLunCalInfo(@NotNull final Year year, @NotNull final Month month,
                              @NotNull final Consumer<? super Body.Item> consumer) {
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getLunCalInfo(year, month, null, pageNo);
            final List<Body.Item> items = getItems(entity);
            items.forEach(consumer);
            if (items.isEmpty()) {
                break;
            }
        }
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
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
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
