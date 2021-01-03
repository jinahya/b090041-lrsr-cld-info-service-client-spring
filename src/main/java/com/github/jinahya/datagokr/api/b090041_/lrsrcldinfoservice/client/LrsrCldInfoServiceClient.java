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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

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
    protected @NotNull Response getResponse(@NotNull final ResponseEntity<Response> responseEntity) {
        requireNonNull(responseEntity, "responseEntity is null");
        final HttpStatus statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("unsuccessful response status code: " + statusCode);
        }
        final Response response = responseEntity.getBody();
        if (response == null) {
            throw new RuntimeException("null body from the response entity");
        }
        return requireValid(response);
    }

    // -------------------------------------------------------------------------------------------------- /getLunCalInfo

    /**
     * Exchanges for {@code GET /.../getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a response entity of {@link Response}.
     */
    protected @NotNull ResponseEntity<Response> getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Positive @Nullable final Integer solDay, @Positive @Nullable final Integer pageNo) {
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
     * Invokes {@code GET /.../getLunCalInfo} with {@code ?solYear}, {@code ?solMonth}, and {@code ?solDay} derived from
     * specified date of solar calendar and returns all items from all pages.
     *
     * @param solarDate the date from which {@code ?solYear}, {@code ?solMonth}, and {@code ?solDay} are derived.
     * @return a list of all retrieved items.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotEmpty List<@Valid @NotNull Item> getLunCalInfo(@NotNull final LocalDate solarDate) {
        final List<Item> items = new ArrayList<>();
        final Year solYear = Year.from(solarDate);
        final Month solMonth = Month.from(solarDate);
        final int solDay = solarDate.getDayOfMonth();
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getLunCalInfo(solYear, solMonth, solDay, pageNo);
            final Response response = getResponse(entity);
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    /**
     * Invokes {@code GET /.../getLunCalInfo} with {@code ?solYear} and {@code ?solMonth} derived from specified
     * year-month of solar calendar and returns all items from all pages.
     *
     * @param solarYearMonth the year-month from which {@code ?solYear} and {@code ?solMonth} are derived.
     * @return a list of all retrieved items.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotBlank List<@Valid @NotNull Item> getLunCalInfo(@NotNull final YearMonth solarYearMonth) {
        final List<Item> items = new ArrayList<>();
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getLunCalInfo(solYear, solMonth, null, pageNo);
            final Response response = getResponse(entity);
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Exchanges for {@code GET /.../getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a response entity of {@link Response}.
     */
    protected @NotNull ResponseEntity<Response> getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Positive @Nullable final Integer lunDay, @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Item.YEAR_FORMATTER.format(lunYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY,
                                     Optional.ofNullable(lunDay)
                                             .map(v -> MonthDay.of(lunMonth, v))
                                             .map(Item.DAY_FORMATTER::format))
                .queryParamIfPresent("pageNo", Optional.ofNullable(pageNo))
                .encode() // ?ServiceKey
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    /**
     * Retrieves all items from {@code GET /.../getSolCalInfo?lunYear=&lunMonth=&lunDay=} with parameters derived from
     * specified lunar date.
     *
     * @param lunarDate the date from which {@code ?lunYear}, {@code ?lunMonth}, and {@code ?lunDay} are derived.
     * @return a list of items retrieved from all pages.
     */
    public @NotNull List<@Valid @NotNull Item> getSolCalInfo(@NotNull final LocalDate lunarDate) {
        final List<Item> items = new ArrayList<>();
        final Year lunYear = Year.from(lunarDate);
        final Month lunMonth = Month.from(lunarDate);
        final int lunDay = lunarDate.getDayOfMonth();
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getSolCalInfo(lunYear, lunMonth, lunDay, pageNo);
            final Response response = getResponse(entity);
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    /**
     * Retrieves all items from {@code GET /.../getSolCalInfo?lunYear=&lunMonth=} with parameters derived from specified
     * lunar date.
     *
     * @param lunarYearMonth the date from which {@code ?lunYear} and {@code ?lunMonth} are derived.
     * @return a list of items retrieved from all pages.
     */
    public @NotNull List<@Valid @NotNull Item> getSolCalInfo(@NotNull final YearMonth lunarYearMonth) {
        final List<Item> items = new ArrayList<>();
        final Year lunYear = Year.from(lunarYearMonth);
        final Month lunMonth = Month.from(lunarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getSolCalInfo(lunYear, lunMonth, null, pageNo);
            final Response response = getResponse(entity);
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves all items from {@code .../getSpcifyLunCalInfo?fromSolYear=&toSolYear&lunYear=&lunMonth=&lunDay=&leapMonth=}
     * with specified arguments.
     *
     * @param fromSolYear a value for {@code ?fromSolYear}.
     * @param toSolYear   a value for {@code ?toSolYear}.
     * @param lunMonth    a value for {@code ?lunMonth}.
     * @param lunDay      a value for {@code ?lunDay}.
     * @param leapMonth   a value for {@code ?leapMonth}.
     * @return a list items retrieved from all pages.
     */
    public @NotNull List<@Valid @NotNull Item> getSpcifyLunCalInfo(
            @Positive final Year fromSolYear, @Positive final Year toSolYear, @NotNull final Month lunMonth,
            @Max(30) @Min(1) final int lunDay, final boolean leapMonth) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        final List<Item> items = new ArrayList<>();
        final String fromSolYearValue = Item.YEAR_FORMATTER.format(fromSolYear);
        final String toSolYearValue = Item.YEAR_FORMATTER.format(toSolYear);
        final String lunMonthValue = Item.MONTH_FORMATTER.format(lunMonth);
        final String lunDayValue = Item.DAY_FORMATTER.format(MonthDay.of(lunMonth, lunDay));
        final String leapMonthValue = leapMonth ? Item.LEAP : Item.NORMAL;
        for (int pageNo = 1; ; pageNo++) {
            final URI url = uriBuilderFromRootUri()
                    .pathSegment(PATH_SEGMENT_GET_SPCIFY_LUN_CAL_INFO)
                    .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                    .queryParam(QUERY_PARAM_NAME_FROM_SOL_YEAR, fromSolYearValue)
                    .queryParam(QUERY_PARAM_NAME_TO_SOL_YEAR, toSolYearValue)
                    .queryParam(QUERY_PARAM_NAME_LUN_MONTH, lunMonthValue)
                    .queryParam(QUERY_PARAM_NAME_LUN_DAY, lunDayValue)
                    .queryParam(QUERY_PARAM_NAME_LEAP_MONTH, leapMonthValue)
                    .queryParam(QUERY_PARAM_NAME_PAGE_NO, pageNo)
                    .encode()
                    .build()
                    .toUri();
            final ResponseEntity<Response> entity = restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
            final Response response = getResponse(entity);
            items.addAll(response.getBody().getItems());
            if (response.getBody().isLastPage()) {
                break;
            }
        }
        return items;
    }

    // -----------------------------------------------------------------------------------------------------------------
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
     * The root uri expanded with {@code '/'} from {@code restTemplate.uriTemplateHandler}.
     */
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private URI rootUri;

    /**
     * A custom root uri value for non-spring-boot environments.
     */
    @LrsrCldInfoServiceRestTemplateRootUri
    @Autowired(required = false)
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private String restTemplateRootUri;
}
