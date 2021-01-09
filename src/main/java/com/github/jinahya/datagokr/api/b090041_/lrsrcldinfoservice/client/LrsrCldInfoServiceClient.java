package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response.Body.Item;
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
import org.springframework.validation.annotation.Validated;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    @PostConstruct
    private void onPostConstruct() {
        rootUri = restTemplate.getUriTemplateHandler().expand("/");
        if (restTemplateRootUri != null) {
            log.info("custom root uri specified: {}", restTemplateRootUri);
            rootUri = URI.create(restTemplateRootUri);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns the body of specified response entity while validating it.
     *
     * @param responseEntity the response entity.
     * @return the body of the {@code responseEntity}.
     */
    protected @NotNull Response getResponse(@NotNull final ResponseEntity<Response> responseEntity) {
        Objects.requireNonNull(responseEntity, "responseEntity is null");
        final HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("unsuccessful response status code: " + statusCode);
        }
        final Response response = responseEntity.getBody();
        if (response == null) {
            throw new RuntimeException("null body from the response entity");
        }
        return requireResultSuccessful(response);
    }

    // -------------------------------------------------------------------------------------------------- /getLunCalInfo

    /**
     * Exchanges a response from {@code /getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a response entity of response.
     */
    protected @NotNull ResponseEntity<Response> getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(31) @Min(1) @Nullable final Integer solDay, @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_LUN_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, Item.YEAR_FORMATTER.format(solYear))
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, Item.MONTH_FORMATTER.format(solMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_SOL_DAY,
                                     Optional.ofNullable(solDay)
                                             .map(v -> MonthDay.of(solMonth, v))
                                             .map(Item.DAY_FORMATTER::format))
                .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    /**
     * Exchanges all items from {@code /getLunCalInfo} for specified date in solar calendar.
     *
     * @param solarDate the date from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}, {@link
     *                  #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}, and {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay} are
     *                  derived.
     * @return a list of all items from all pages.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getLunCalInfo(@NotNull final LocalDate solarDate) {
        final List<Item> items = new ArrayList<>();
        final Year solYear = Year.from(solarDate);
        final Month solMonth = Month.from(solarDate);
        final int solDay = solarDate.getDayOfMonth();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getResponse(getLunCalInfo(solYear, solMonth, solDay, pageNo));
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        assert !items.isEmpty();
        assert items.size() == 1;
        return items;
    }

    /**
     * Exchanges all items from {@code /getLunCalInfo} for specified solar month.
     *
     * @param solarMonth the solar month from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear} and {@link
     *                   #QUERY_PARAM_NAME_SOL_MONTH ?solMonth} are derived.
     * @return a list of all retrieved items.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getLunCalInfo(@NotNull final YearMonth solarMonth) {
        final List<Item> items = new ArrayList<>();
        final Year solYear = Year.from(solarMonth);
        final Month solMonth = Month.from(solarMonth);
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getResponse(getLunCalInfo(solYear, solMonth, null, pageNo));
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Exchanges a response from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a response entity of response.
     */
    protected @NotNull ResponseEntity<Response> getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(30) @Min(1) @Nullable final Integer lunDay, @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Item.YEAR_FORMATTER.format(lunYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY, Optional.ofNullable(lunDay).map(Item::formatDay))
                .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    /**
     * Exchanges all items from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunarYear       a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunarMonth      a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunarDayOfMonth a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @return a list of all items from all pages.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public @Size(min = 1, max = 2) @NotNull List<@Valid @NotNull Item> getSolCalInfo(
            @NotNull final Year lunarYear, @NotNull final Month lunarMonth,
            @Max(30) @Min(1) final int lunarDayOfMonth) {
        final List<Item> items = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getResponse(getSolCalInfo(lunarYear, lunarMonth, lunarDayOfMonth, pageNo));
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        assert !items.isEmpty();
        assert items.size() <= 2;
        return items;
    }

    /**
     * Exchanges all items from {@code /getSolCalInfo} with parameters derived from specified lunar month.
     *
     * @param lunarYearMonth the lunar month from which {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear} and {@link
     *                       #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth} are derived.
     * @return a list of items from all pages.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public @NotNull List<@Valid @NotNull Item> getSolCalInfo(@NotNull final YearMonth lunarYearMonth) {
        final List<Item> items = new ArrayList<>();
        final Year lunYear = Year.from(lunarYearMonth);
        final Month lunMonth = Month.from(lunarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getResponse(getSolCalInfo(lunYear, lunMonth, null, pageNo));
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Exchanges a response from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @param pageNo      a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a response entity of response.
     */
    protected @NotNull ResponseEntity<Response> getSpcifyLunCalInfo(
            @Positive final Year fromSolYear, @Positive final Year toSolYear, @NotNull final Month lunMonth,
            @Max(30) @Min(1) final int lunDay, final boolean leapMonth, @Positive int pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SPCIFY_LUN_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_FROM_SOL_YEAR, Item.YEAR_FORMATTER.format(fromSolYear))
                .queryParam(QUERY_PARAM_NAME_TO_SOL_YEAR, Item.YEAR_FORMATTER.format(toSolYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
                .queryParam(QUERY_PARAM_NAME_LUN_DAY, Item.DAY_FORMATTER.format(MonthDay.of(lunMonth, lunDay)))
                .queryParam(QUERY_PARAM_NAME_LEAP_MONTH, leapMonth ? Item.LEAP : Item.NORMAL)
                .queryParam(QUERY_PARAM_NAME_PAGE_NO, pageNo)
                .encode()
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    /**
     * Exchanges all items from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a list of all items from all pages.
     * @see #getSpcifyLunCalInfo(Year, Year, Month, int, boolean, int)
     */
    public @NotNull List<@Valid @NotNull Item> getSpcifyLunCalInfo(
            @Positive final Year fromSolYear, @Positive final Year toSolYear, @NotNull final Month lunMonth,
            @Max(30) @Min(1) final int lunDay, final boolean leapMonth) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        final List<Item> items = new ArrayList<>();
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getResponse(
                    getSpcifyLunCalInfo(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth, pageNo));
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
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

    // -----------------------------------------------------------------------------------------------------------------
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
