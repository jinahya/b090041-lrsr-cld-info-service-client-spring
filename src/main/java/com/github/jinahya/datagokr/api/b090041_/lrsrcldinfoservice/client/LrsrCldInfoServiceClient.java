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
import javax.validation.constraints.NotBlank;
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
    protected @NotNull ResponseEntity<Response> getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Positive @Nullable final Integer solDay, @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment("getLunCalInfo")
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_SOL_YEAR, Response.Body.Item.YEAR_FORMATTER.format(solYear))
                .queryParam(QUERY_PARAM_NAME_SOL_MONTH, Response.Body.Item.MONTH_FORMATTER.format(solMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_SOL_DAY,
                                     Optional.ofNullable(solDay)
                                             .map(v -> MonthDay.of(solMonth, v))
                                             .map(Response.Body.Item.DAY_FORMATTER::format))
                .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                .encode()
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    public @Valid @NotNull Response.Body.Item getLunCalInfo(@NotNull final LocalDate solarDate) {
        final Year solYear = Year.from(solarDate);
        final Month solMonth = Month.from(solarDate);
        final int solDay = solarDate.getDayOfMonth();
        final ResponseEntity<Response> entity = getLunCalInfo(solYear, solMonth, solDay, null);
        final List<Response.Body.Item> items = getItems(entity);
        if (items.isEmpty()) {
            throw new RuntimeException("no items in response");
        }
        assert items.size() == 1;
        return items.get(0);
    }

    /**
     * Invokes {@code GET /.../getLunCalInfo} with {@code ?solYear} and {@code ?solMonth} derived from specified
     * year-month of solar calendar and returns all items from all pages.
     *
     * @param solarYearMonth the year-month from which {@code ?solYear} and {@code ?solMonth} are derived.
     * @return a list of all retrieved items.
     */
    public @NotBlank List<Response.Body.Item> getLunCalInfo(@NotNull final YearMonth solarYearMonth) {
        final List<Response.Body.Item> all = new ArrayList<>();
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getLunCalInfo(solYear, solMonth, null, pageNo);
            final List<Response.Body.Item> items = getItems(entity);
            if (items.isEmpty()) {
                break;
            }
            all.addAll(items);
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------
    protected @NotNull ResponseEntity<Response> getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Positive @Nullable final Integer lunDay, @Positive @Nullable final Integer pageNo) {
        final URI url = uriBuilderFromRootUri()
                .pathSegment("getSolCalInfo")
                .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Response.Body.Item.YEAR_FORMATTER.format(lunYear))
                .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Response.Body.Item.MONTH_FORMATTER.format(lunMonth))
                .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY,
                                     Optional.ofNullable(lunDay)
                                             .map(v -> MonthDay.of(lunMonth, v))
                                             .map(Response.Body.Item.DAY_FORMATTER::format))
                .queryParamIfPresent("pageNo", Optional.ofNullable(pageNo))
                .encode()
                .build()
                .toUri();
        return restTemplate().exchange(url, HttpMethod.GET, null, Response.class);
    }

    public @Valid @NotNull Response.Body.Item getSolCalInfo(@NotNull final LocalDate lunarDate) {
        final Year lunYear = Year.from(lunarDate);
        final Month lunMonth = Month.from(lunarDate);
        final int lunDay = lunarDate.getDayOfMonth();
        final ResponseEntity<Response> responseEntity = getSolCalInfo(lunYear, lunMonth, lunDay, null);
        final List<Response.Body.Item> items = getItems(responseEntity);
        if (items.isEmpty()) {
            throw new RuntimeException("no items in response");
        }
        assert items.size() == 1;
        return items.get(0);
    }

    /**
     * Invokes {@code GET /.../getSolCalInfo} with {@code ?lunYear} and {@code ?lunMonth} derived from specified
     * year-month of lunar calendar and returns all items from all pages.
     *
     * @param lunarYearMonth the year-month from which {@code ?lunYear} and {@code ?lunMonth} are derived.
     * @return a list of all retrieved items.
     */
    public @NotBlank List<Response.Body.@Valid @NotNull Item> getSolCalInfo(@NotNull final YearMonth lunarYearMonth) {
        final List<Response.Body.Item> all = new ArrayList<>();
        final Year lunYear = Year.from(lunarYearMonth);
        final Month lunMonth = Month.from(lunarYearMonth);
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = getSolCalInfo(lunYear, lunMonth, null, pageNo);
            final List<Response.Body.Item> items = getItems(entity);
            if (items.isEmpty()) {
                break;
            }
            all.addAll(items);
        }
        return all;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public @Positive int getSpcifyLunCalInfo(@Positive final Year fromSolYear, @Positive final Year toSolYear,
                                             @NotNull final Month lunMonth, @Max(31) @Min(1) final int lunDay,
                                             final boolean leapMonth,
                                             @NotNull final Consumer<? super Response.Body.Item> itemConsumer) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        int count = 0;
        final String fromSolYearValue = Response.Body.Item.YEAR_FORMATTER.format(fromSolYear);
        final String toSolYearValue = Response.Body.Item.YEAR_FORMATTER.format(toSolYear);
        final String lunMonthValue = Response.Body.Item.MONTH_FORMATTER.format(lunMonth);
        final String lunDayValue = Response.Body.Item.DAY_FORMATTER.format(MonthDay.of(lunMonth, lunDay));
        final String leapMonthValue = leapMonth ? Response.Body.Item.LEAP : Response.Body.Item.NORMAL;
        for (int pageNo = 1; ; pageNo++) {
            final URI url = uriBuilderFromRootUri()
                    .pathSegment("getSpcifyLunCalInfo")
                    .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                    .queryParam("fromSolYear", fromSolYearValue)
                    .queryParam("toSolYear", toSolYearValue)
                    .queryParam("lunMonth", lunMonthValue)
                    .queryParam("lunDay", lunDayValue)
                    .queryParam("leapMonth", leapMonthValue)
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
