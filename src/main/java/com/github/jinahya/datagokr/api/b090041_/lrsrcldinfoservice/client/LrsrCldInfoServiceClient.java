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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
    protected @NotNull List<Response.Body.@NotNull @Valid Item> getItems(
            @NotNull final ResponseEntity<Response> entity) {
        requireNonNull(entity, "entity is null");
        {
            final HttpStatus statusCode = entity.getStatusCode();
            if (!statusCode.is2xxSuccessful()) {
                throw new RuntimeException("unsuccessful response status code: " + statusCode);
            }
        }
        final Response response = entity.getBody();
        if (response == null) {
            throw new RuntimeException("null body from the response entity");
        }
        return getItems(response);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    protected ResponseEntity<Response> getLunCalInfo(@NotNull final Year solarYear, @NotNull final Month solarMonth,
                                                     @Positive @Nullable final Integer solarDayOfMonth,
                                                     @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment("getLunCalInfo")
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, Response.Body.Item.YEAR_FORMATTER.format(solarYear))
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, Response.Body.Item.MONTH_FORMATTER.format(solarMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_SOL_DAY,
                                     Optional.ofNullable(solarDayOfMonth)
                                             .map(v -> MonthDay.of(solarMonth, v))
                                             .map(Response.Body.Item.DAY_FORMATTER::format))
                .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                .encode()
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    @Valid
    @NotNull
    public Response.Body.Item getLunCalInfo(@NotNull final LocalDate solarDate) {
        final Year solarYear = Year.from(solarDate);
        final Month solarMonth = Month.from(solarDate);
        final int solarDayOfMonth = solarDate.getDayOfMonth();
        final ResponseEntity<Response> entity = getLunCalInfo(solarYear, solarMonth, solarDayOfMonth, null);
        final List<Response.Body.Item> items = getItems(entity);
        if (items.isEmpty()) {
            throw new RuntimeException("no items in response");
        }
        assert items.size() == 1;
        return items.get(0);
    }

    /**
     * Reads all dates in lunar calendar associated with specified month in solar calendar and accepts each of them to
     * specified consumer.
     *
     * @param solarYearMonth the month (in solar calendar) whose dates (in lunar calendar) are read.
     * @param itemConsumer   the consumer to which each date (in lunar calendar) are accepted.
     * @return the number of items accepted to {@code itemConsumer}.
     */
    public int getLunCalInfo(@NotNull final YearMonth solarYearMonth,
                             @NotNull final Consumer<? super Response.Body.Item> itemConsumer) {
        int count = 0;
        final Year solarYear = Year.from(solarYearMonth);
        final Month solarMonth = Month.from(solarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getLunCalInfo(solarYear, solarMonth, null, pageNo);
            final List<Response.Body.Item> items = getItems(entity);
            if (items.isEmpty()) {
                break;
            }
            items.forEach(itemConsumer);
            count += items.size();
        }
        return count;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NotNull
    protected ResponseEntity<Response> getSolCalInfo(@NotNull final Year lunarYear, @NotNull final Month lunarMonth,
                                                     @Positive @Nullable final Integer lunarDayOfMonth,
                                                     @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment("getSolCalInfo")
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Response.Body.Item.YEAR_FORMATTER.format(lunarYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Response.Body.Item.MONTH_FORMATTER.format(lunarMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY,
                                     Optional.ofNullable(lunarDayOfMonth)
                                             .map(v -> MonthDay.of(lunarMonth, v))
                                             .map(Response.Body.Item.DAY_FORMATTER::format))
                .queryParamIfPresent("pageNo", Optional.ofNullable(pageNo))
                .encode()
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    @Valid
    @NotNull
    public Response.Body.Item getSolCalInfo(@NotNull final LocalDate lunarDate) {
        final Year lunarYear = Year.from(lunarDate);
        final Month lunarMonth = Month.from(lunarDate);
        final int lunarDayOfMonth = lunarDate.getDayOfMonth();
        final ResponseEntity<Response> responseEntity = getSolCalInfo(lunarYear, lunarMonth, lunarDayOfMonth, null);
        final List<Response.Body.Item> items = getItems(responseEntity);
        if (items.isEmpty()) {
            throw new RuntimeException("no items in response");
        }
        assert items.size() == 1;
        return items.get(0);
    }

    /**
     * Reads all dates in solar calendar associated with specified month in lunar calendar and accepts each of them to
     * specified consumer.
     *
     * @param lunarYearMonth the month (in lunar calendar) whose dates (in solar calendar) are read.
     * @param itemConsumer   the consumer to which each date (in solar calendar) are accepted.
     * @return the number of items accepted to {@code itemConsumer}.
     */
    public int getSolCalInfo(@NotNull final YearMonth lunarYearMonth,
                             @NotNull final Consumer<? super Response.Body.Item> itemConsumer) {
        int count = 0;
        final Year lunarYear = Year.from(lunarYearMonth);
        final Month lunarMonth = Month.from(lunarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getSolCalInfo(lunarYear, lunarMonth, null, pageNo);
            final List<Response.Body.Item> items = getItems(entity);
            if (items.isEmpty()) {
                break;
            }
            items.forEach(itemConsumer);
            count += items.size();
        }
        return count;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public @Positive int getSpcifyLunCalInfo(@Positive final Year fromSolarYear, @Positive final Year toSolarYear,
                                             @NotNull final Month lunarMonth,
                                             @Max(31) @Min(1) final int lunarDayOfMonth, final boolean lunarLeapMonth,
                                             @NotNull final Consumer<? super Response.Body.Item> itemConsumer) {
        if (toSolarYear.isBefore(fromSolarYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolarYear + ") is before fromSolYear(" + fromSolarYear + ")");
        }
        int count = 0;
        final String fromSolarYearValue = Response.Body.Item.YEAR_FORMATTER.format(fromSolarYear);
        final String toSolarYearValue = Response.Body.Item.YEAR_FORMATTER.format(toSolarYear);
        final String lunarMonthValue = Response.Body.Item.MONTH_FORMATTER.format(lunarMonth);
        final String lunarDayOfMonthValue
                = Response.Body.Item.DAY_FORMATTER.format(MonthDay.of(lunarMonth, lunarDayOfMonth));
        final String lunarLeapMonthValue = lunarLeapMonth ? Response.Body.Item.LEAP : Response.Body.Item.NORMAL;
        for (int pageNo = 1; ; pageNo++) {
            final URI url = uriBuilderFromRootUri()
                    .pathSegment("getSpcifyLunCalInfo")
                    .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                    .queryParam("fromSolYear", fromSolarYearValue)
                    .queryParam("toSolYear", toSolarYearValue)
                    .queryParam("lunMonth", lunarMonthValue)
                    .queryParam("lunDay", lunarDayOfMonthValue)
                    .queryParam("leapMonth", lunarLeapMonthValue)
                    .queryParam("pageNo", pageNo)
                    .encode()
                    .build()
                    .toUri();
            final ResponseEntity<Response> entity = restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
            final List<Response.Body.Item> items = getItems(entity);
            if (items.isEmpty()) {
                break;
            }
            items.forEach(itemConsumer);
            count += items.size();
        }
        return count;
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
