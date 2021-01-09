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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A client implementation uses an instance of {@link WebClient}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see LrsrCldInfoServiceClient
 */
@Lazy
@Validated
@Component
@Slf4j
public class LrsrCldInfoServiceReactiveClient extends AbstractLrsrCldInfoServiceClient {

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An injection qualifier for an instance of {@link WebClient}.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceWebClient {

    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a response from {@code /getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a mono of response.
     */
    protected @NotNull Mono<Response> getLunCalInfo(@NotNull final Year solYear, @NotNull final Month solMonth,
                                                    @Max(31) @Min(1) @Nullable final Integer solDay,
                                                    @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> b.pathSegment(PATH_SEGMENT_GET_LUN_CAL_INFO)
                        .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                        .queryParam(QUERY_PARAM_NAME_SOL_YEAR, Item.YEAR_FORMATTER.format(solYear))
                        .queryParam(QUERY_PARAM_NAME_SOL_MONTH, Item.MONTH_FORMATTER.format(solMonth))
                        .queryParamIfPresent(QUERY_PARAM_NAME_SOL_DAY,
                                             Optional.ofNullable(solDay).map(v -> MonthDay.of(solMonth, v))
                                                     .map(Item.DAY_FORMATTER::format))
                        .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                        .build())
                .retrieve()
                .bodyToMono(Response.class)
                .map(this::requireResultSuccessful)
                ;
    }

    /**
     * Retrieves all items from {@code /.../getLunCalInfo} with parameters derived from specified date in solar
     * calendar.
     *
     * @param solarDate the date from which {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}, {@link
     *                  #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}, and {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay} are
     *                  derived.
     * @return a flux of all items from all pages.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public Flux<Item> getLunCalInfo(@NotNull final LocalDate solarDate) {
        final Year solYear = Year.from(solarDate);
        final Month solMonth = Month.from(solarDate);
        final int solDay = solarDate.getDayOfMonth();
        final AtomicInteger pageNo = new AtomicInteger();
        return getLunCalInfo(solYear, solMonth, solDay, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getLunCalInfo(solYear, solMonth, solDay, pageNo.incrementAndGet());
                })
                .flatMap(r -> Flux.fromIterable(r.getBody().getItems()));
    }

    /**
     * Retrieves all items from {@code /getLunCalInfo} with arguments derived from specified month in solar calendar.
     *
     * @param solarYearMonth the month from which {@code ?solYear} and {@code ?solMonth} are derived.
     * @return a flux of all items from all pages.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public Flux<Item> getLunCalInfo(@NotNull final YearMonth solarYearMonth) {
        final Year solYear = Year.from(solarYearMonth);
        final Month solMonth = Month.from(solarYearMonth);
        final AtomicInteger pageNo = new AtomicInteger();
        return getLunCalInfo(solYear, solMonth, null, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getLunCalInfo(solYear, solMonth, null, pageNo.incrementAndGet());
                })
                .flatMap(r -> Flux.fromIterable(r.getBody().getItems()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a response from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a mono of response.
     */
    protected @NotNull Mono<Response> getSolCalInfo(@NotNull final Year lunYear, @NotNull final Month lunMonth,
                                                    @Max(30) @Min(1) @Nullable final Integer lunDay,
                                                    @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> b.pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                        .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                        .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Item.YEAR_FORMATTER.format(lunYear))
                        .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
                        .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY, Optional.ofNullable(lunDay).map(Item::formatDay))
                        .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, Optional.ofNullable(pageNo))
                        .build())
                .retrieve()
                .bodyToMono(Response.class)
                .map(this::requireResultSuccessful)
                ;
    }

    /**
     * Retrieves all items from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunarYear       a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunarMonth      a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunarDayOfMonth a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @return a flux of items from all pages.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public Flux<Item> getSolCalInfo(@NotNull final Year lunarYear, @NotNull final Month lunarMonth,
                                    @Max(30) @Min(1) @Nullable final Integer lunarDayOfMonth) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getSolCalInfo(lunarYear, lunarMonth, lunarDayOfMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getSolCalInfo(lunarYear, lunarMonth, lunarDayOfMonth, pageNo.incrementAndGet());
                })
                .flatMap(r -> Flux.fromIterable(r.getBody().getItems()));
    }

    /**
     * Retrieves all items from {@code /getSolCalInfo} with parameters derived from specified month in lunar calendar.
     *
     * @param lunarMonth the month from which {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear} and {@link
     *                   #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth} are derived.
     * @return a flux of all items from all pages.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public Flux<Item> getSolCalInfo(@NotNull final YearMonth lunarMonth) {
        final Year lunYear = Year.from(lunarMonth);
        final Month lunMonth = Month.from(lunarMonth);
        final AtomicInteger pageNo = new AtomicInteger();
        return getSolCalInfo(lunYear, lunMonth, null, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getSolCalInfo(lunYear, lunMonth, null, pageNo.incrementAndGet());
                })
                .flatMap(r -> Flux.fromIterable(r.getBody().getItems()));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a response from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @param pageNo      a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}.
     * @return a mono of response.
     */
    public Mono<Response> getSpcifyLunCalInfo(@Positive final Year fromSolYear, @Positive final Year toSolYear,
                                              @NotNull final Month lunMonth, @Max(30) @Min(1) final int lunDay,
                                              final boolean leapMonth, @Positive final int pageNo) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        final String fromSolYearValue = Item.YEAR_FORMATTER.format(fromSolYear);
        final String toSolYearValue = Item.YEAR_FORMATTER.format(toSolYear);
        final String lunMonthValue = Item.MONTH_FORMATTER.format(lunMonth);
        final String lunDayValue = Item.DAY_FORMATTER.format(MonthDay.of(lunMonth, lunDay));
        final String leapMonthValue = leapMonth ? Item.LEAP : Item.NORMAL;
        return webClient
                .get()
                .uri(b -> b
                        .pathSegment(PATH_SEGMENT_GET_SPCIFY_LUN_CAL_INFO)
                        .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                        .queryParam(QUERY_PARAM_NAME_FROM_SOL_YEAR, fromSolYearValue)
                        .queryParam(QUERY_PARAM_NAME_TO_SOL_YEAR, toSolYearValue)
                        .queryParam(QUERY_PARAM_NAME_LUN_MONTH, lunMonthValue)
                        .queryParam(QUERY_PARAM_NAME_LUN_DAY, lunDayValue)
                        .queryParam(QUERY_PARAM_NAME_LEAP_MONTH, leapMonthValue)
                        .queryParam(QUERY_PARAM_NAME_PAGE_NO, pageNo)
                        .build()
                )
                .retrieve()
                .bodyToMono(Response.class)
                .map(this::requireResultSuccessful)
                ;
    }

    /**
     * Retrieves all items from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a flux of all items from all pages.
     * @see #getSpcifyLunCalInfo(Year, Year, Month, int, boolean, int)
     */
    public Flux<Item> getSpcifyLunCalInfo(@Positive final Year fromSolYear, @Positive final Year toSolYear,
                                          @NotNull final Month lunMonth, @Max(30) @Min(1) final int lunDay,
                                          final boolean leapMonth) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        final AtomicInteger pageNo = new AtomicInteger(0);
        return getSpcifyLunCalInfo(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getSpcifyLunCalInfo(
                            fromSolYear, toSolYear, lunMonth, lunDay, leapMonth, pageNo.incrementAndGet());
                })
                .flatMap(r -> Flux.fromIterable(r.getBody().getItems()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    @LrsrCldInfoServiceWebClient
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private WebClient webClient;
}
