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
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
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
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @Valid @NotNull Response getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(31) @Min(1) @Nullable final Integer solDay, @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_LUN_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, Item.YEAR_FORMATTER.format(solYear))
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, Item.MONTH_FORMATTER.format(solMonth))
//                .queryParamIfPresent(QUERY_PARAM_NAME_SOL_DAY,
//                                     Optional.ofNullable(solDay)
//                                             .map(v -> MonthDay.of(solMonth, v))
//                                             .map(Item.DAY_FORMATTER::format))
//                .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                ;
        ofNullable(solDay)
                .map(v -> MonthDay.of(solMonth, v))
                .map(Item.DAY_FORMATTER::format)
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
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}.
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
     * @param solarDate the date from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}, {@link
     *                  #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}, and {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay} are
     *                  derived.
     * @return a list of all items from all pages.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @Size(min = 1, max = 1) List<@Valid @NotNull Item> getLunCalInfo(@NotNull final LocalDate solarDate) {
        final Year solYear = Year.from(solarDate);
        final Month solMonth = Month.from(solarDate);
        final int solDay = solarDate.getDayOfMonth();
        return getLunCalInfoForAllPages(solYear, solMonth, solDay)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items from {@code /getLunCalInfo} for specified month in solar.
     *
     * @param solarYearMonth the solar month from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear} and {@link
     *                       #QUERY_PARAM_NAME_SOL_MONTH ?solMonth} are derived.
     * @return a list of items.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getLunCalInfo(@NotNull final YearMonth solarYearMonth) {
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        return getLunCalInfoForAllPages(solYear, solMonth, null)
                .stream().flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified solar year.
     *
     * @param year       the solar year.
     * @param executor   an executor for concurrently execute {@link #getLunCalInfo(YearMonth)} for each {@link Month}
     *                   in {@code year}.
     * @param collection a collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getLunCalInfo(YearMonth)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getLunCalInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> YearMonth.of(year.getValue(), v))
                .map(v -> supplyAsync(() -> getLunCalInfo(v), executor))
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

    // -------------------------------------------------------------------------------------------------- /getSolCalInfo

    /**
     * Retrieves a response from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return the response.
     */
    public @NotNull Response getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay,
            @Positive @Nullable final Integer pageNo) {
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Item.YEAR_FORMATTER.format(lunYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
//                .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY, Optional.ofNullable(lunDay).map(Item::formatDay))
//                .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                ;
        ofNullable(lunDay)
                .map(Item::formatDay)
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
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @return a list of responses.
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
     * @param lunarYear       a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunarMonth      a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunarDayOfMonth a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @return a list of items.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public @Size(min = 1, max = 2) @NotNull List<@Valid @NotNull Item> getSolCalInfo(
            @NotNull final Year lunarYear, @NotNull final Month lunarMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunarDayOfMonth) {
        return getSolCalInfoForAllPages(lunarYear, lunarMonth, lunarDayOfMonth)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Retrieves all items from {@code /getSolCalInfo} with parameters derived from specified month in lunar calendar.
     *
     * @param lunarYearMonth the lunar month from which {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear} and {@link
     *                       #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth} are derived.
     * @return a list of items.
     * @see #getSolCalInfoForAllPages(Year, Month, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getSolCalInfo(@NotNull final YearMonth lunarYearMonth) {
        final Year lunYear = Year.from(lunarYearMonth);
        final Month lunMonth = Month.from(lunarYearMonth);
        return getSolCalInfoForAllPages(lunYear, lunMonth, null)
                .stream()
                .flatMap(r -> r.getBody().getItems().stream())
                .collect(toList());
    }

    /**
     * Reads all items for specified lunar year and adds them to specified collection.
     *
     * @param year       the lunar year.
     * @param executor   an executor for concurrently executing {@link #getSolCalInfo(YearMonth)} for each {@link Month}
     *                   in {@code year}.
     * @param collection the collection to which retrieved items are added.
     * @param <T>        collection type parameter
     * @return given {@code collection}.
     * @see #getSolCalInfo(YearMonth)
     */
    @NotEmpty
    public <T extends Collection<? super Item>> T getSolCalInfo(
            @NotNull final Year year, @NotNull final Executor executor, @NotNull final T collection) {
        Arrays.stream(Month.values())
                .map(v -> YearMonth.of(year.getValue(), v))
                .map(v -> supplyAsync(() -> getSolCalInfo(v), executor))
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
                    "toSolYear(" + toSolYear + ") is before fromSolarYear(" + fromSolYear + ")");
        }
        final UriComponentsBuilder builder = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SPCIFY_LUN_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_FROM_SOL_YEAR, Item.YEAR_FORMATTER.format(fromSolYear))
                .queryParam(QUERY_PARAM_NAME_TO_SOL_YEAR, Item.YEAR_FORMATTER.format(toSolYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
                .queryParam(QUERY_PARAM_NAME_LUN_DAY, Item.DAY_FORMATTER.format(MonthDay.of(lunMonth, lunDay)))
                .queryParam(QUERY_PARAM_NAME_LEAP_MONTH, leapMonth ? Item.LEAP : Item.NON_LEAP);
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
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolarYear(" + fromSolYear + ")");
        }
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
     * @param fromSolarYear   a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolarYear     a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunarMonth      a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunarDayOfMonth a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param lunarLeapMonth  a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a list of items.
     * @see #getSpcifyLunCalInfoForAllPages(Year, Year, Month, int, boolean)
     */
    public @NotNull List<@Valid @NotNull Item> getSpcifyLunCalInfo(
            @NotNull final Year fromSolarYear, @NotNull final Year toSolarYear, @NotNull final Month lunarMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunarDayOfMonth,
            final boolean lunarLeapMonth) {
        return getSpcifyLunCalInfoForAllPages(fromSolarYear, toSolarYear, lunarMonth, lunarDayOfMonth, lunarLeapMonth)
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
    @Autowired
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
    @Getter(AccessLevel.NONE)
    private String restTemplateRootUri;

    /**
     * The root uri expanded with {@code '/'} from {@code restTemplate.uriTemplateHandler}.
     */
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private URI rootUri;
}
