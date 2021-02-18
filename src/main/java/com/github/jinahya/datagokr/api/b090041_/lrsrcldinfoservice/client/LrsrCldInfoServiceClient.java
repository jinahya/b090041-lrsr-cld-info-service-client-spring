package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MAX_DAY_OF_MONTH_LUNAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MAX_DAY_OF_MONTH_SOLAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MIN_DAY_OF_MONTH_LUNAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MIN_DAY_OF_MONTH_SOLAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Responses.requireResultSuccessful;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

/**
 * A client implementation uses an instance of {@link RestTemplate}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see LrsrCldInfoServiceReactiveClient
 */
@Lazy
@Component
@Slf4j
public class LrsrCldInfoServiceClient extends AbstractLrsrCldInfoServiceClient {

    /**
     * An injection qualifier for an instance of {@link RestTemplate}.
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

    /**
     * Returns the body of specified response entity while validating it.
     *
     * @param responseEntity the response entity.
     * @return the body of the {@code responseEntity}.
     */
    protected static @NotNull Response unwrap(@NotNull final ResponseEntity<Response> responseEntity) {
        Objects.requireNonNull(responseEntity, "responseEntity is null");
        final HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RestClientException("unsuccessful response status: " + statusCode);
        }
        final Response response = responseEntity.getBody();
        if (response == null) {
            throw new RestClientException("no entity body received");
        }
        return requireResultSuccessful(response, h -> new RestClientException("unsuccessful result: " + h));
    }

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public LrsrCldInfoServiceClient() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @PostConstruct
    private void onPostConstruct() {
        if (restTemplate == null) {
            log.warn("no rest template autowired. using default instance...");
            restTemplate = new RestTemplate();
            restTemplateRootUri = AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION;
        }
        rootUri = restTemplate.getUriTemplateHandler().expand("/");
        if (restTemplateRootUri != null) {
            log.info("custom root uri specified: {}", restTemplateRootUri);
            rootUri = URI.create(restTemplateRootUri);
        }
    }

    // -------------------------------------------------------------------------------------------------- /getLunCalInfo

    /**
     * Retrieves a response from {@code /getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}; {@code null} for a whole month.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return the response.
     */
    public @Valid @NotNull Response getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(31) @Min(1) @Nullable final Integer solDay, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_LUN_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear.getValue())
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, MONTH_FORMATTER.format(solMonth));
        ofNullable(solDay)
                .map(v -> MonthDay.of(solMonth, v))
                .map(DAY_FORMATTER::format)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_SOL_DAY, v));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}; {@code null} for a whole month.
     * @return a list of responses.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> getLunCalInfoForAllPages(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(MAX_DAY_OF_MONTH_SOLAR) @Min(MIN_DAY_OF_MONTH_SOLAR) @Nullable final Integer solDay) {
        final List<Response> result = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getLunCalInfo(solYear, solMonth, solDay, pageNo);
            result.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return result;
    }

    /**
     * Reads all items from {@code /getLunCalInfo} for specified date in solar calendar.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}; {@code null} for a whole month.
     * @return a list of all items from all pages.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @Size(min = 1, max = 1) List<@Valid @NotNull Item> getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(MAX_DAY_OF_MONTH_SOLAR) @Min(MIN_DAY_OF_MONTH_SOLAR) @Nullable final Integer solDay) {
        return getLunCalInfoForAllPages(solYear, solMonth, solDay)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified solar year.
     *
     * @param year       the solar year.
     * @param executor   an executor for concurrently invoking {@link #getLunCalInfo(Year, Month, Integer)} for each
     *                   {@link Month} in {@code year}.
     * @param collection a collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getLunCalInfo(Year, Month, Integer)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getLunCalInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> supplyAsync(() -> getLunCalInfo(year, v, null), executor))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                })
                .forEach(collection::addAll);
        return collection;
    }

    // -------------------------------------------------------------------------------------------------- /getSolCalInfo

    /**
     * Retrieves a response from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}; {@code null} for a whole month.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return the response.
     */
    public @NotNull Response getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay,
            @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_LUN_YEAR, lunYear.getValue())
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, MONTH_FORMATTER.format(lunMonth));
        ofNullable(lunDay)
                .map(AbstractLrsrCldInfoServiceClient::formatDay)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_LUN_DAY, v));
        ofNullable(pageNo)
                .ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}; {@code null} for a whole year.
     * @return a list of responses.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public @NotNull List<@Valid @NotNull Response> getSolCalInfoForAllPages(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay) {
        final List<Response> responses = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getSolCalInfo(lunYear, lunMonth, lunDay, pageNo);
            responses.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return responses;
    }

    /**
     * Reads all items from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}; {@code null} for a whole month.
     * @return a list of items.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public @NotEmpty @NotNull List<@Valid @NotNull Item> getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay) {
        return getSolCalInfoForAllPages(lunYear, lunMonth, lunDay)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified lunar year and adds them to specified collection.
     *
     * @param year       the lunar year.
     * @param executor   an executor for concurrently executing {@link #getSolCalInfo(Year, Month, Integer)} for each
     *                   {@link Month} in {@code year}.
     * @param collection the collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getSolCalInfo(Year, Month, Integer)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getSolCalInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> supplyAsync(() -> getSolCalInfo(year, v, null), executor))
                .map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                })
                .forEach(collection::addAll)
        ;
        return collection;
    }

    // -------------------------------------------------------------------------------------------- /getSpcifyLunCalInfo

    /**
     * Retrieves a response from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @param pageNo      a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @Valid @NotNull Response getSpcifyLunCalInfo(
            @NotNull final Year fromSolYear, @NotNull final Year toSolYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunDay, final boolean leapMonth,
            @Positive @Nullable Integer pageNo) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SPCIFY_LUN_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_FROM_SOL_YEAR, fromSolYear.getValue())
                .queryParam(QUERY_PARAM_NAME_TO_SOL_YEAR, toSolYear.getValue())
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, MONTH_FORMATTER.format(lunMonth))
                .queryParam(QUERY_PARAM_NAME_LUN_DAY, formatDay(lunDay))
                .queryParam(QUERY_PARAM_NAME_LEAP_MONTH, queryParamValueLeapMonth(leapMonth));
        ofNullable(pageNo).ifPresent(v -> builder.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
        final URI url = builder.encode().build().toUri();
        return unwrap(restTemplate().exchange(url, HttpMethod.GET, null, Response.class));
    }

    /**
     * Reads all responses from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a list of responses.
     */
    public @NotNull List<@Valid @NotNull Response> getSpcifyLunCalInfoForAllPages(
            @NotNull final Year fromSolYear, @NotNull final Year toSolYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunDay, final boolean leapMonth) {
        final List<Response> responses = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getSpcifyLunCalInfo(
                    fromSolYear, toSolYear, lunMonth, lunDay, leapMonth, pageNo);
            responses.add(response);
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return responses;
    }

    /**
     * Reads all items from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a list of items.
     * @see #getSpcifyLunCalInfoForAllPages(Year, Year, Month, int, boolean)
     */
    public @NotNull List<@Valid @NotNull Item> getSpcifyLunCalInfo(
            @NotNull final Year fromSolYear, @NotNull final Year toSolYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunDay,
            final boolean leapMonth) {
        return getSpcifyLunCalInfoForAllPages(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a uri builder built from the {@code rootUri}.
     *
     * @return a uri builder built from the {@code rootUri}.
     */
    protected UriComponentsBuilder uriBuilderFromRootUri() {
        return UriComponentsBuilder.fromUri(rootUri);
    }

    // ------------------------------------------------------------------------------------------------- instance fields
    @LrsrCldInfoServiceRestTemplate
    @Autowired(required = false)
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private RestTemplate restTemplate;

    /**
     * A custom root uri value for non-spring-boot environments.
     */
    @LrsrCldInfoServiceRestTemplateRootUri
    @Autowired(required = false)
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PACKAGE)
    private String restTemplateRootUri;

    /**
     * The root uri expanded with {@code '/'} from {@code restTemplate.uriTemplateHandler}.
     */
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private URI rootUri;
}
