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
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Optional.ofNullable;

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
     * An injection qualifier for an instance of {@link WebClient} for accessing {@link #BASE_URL}.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceWebClient {

    }

    // -----------------------------------------------------------------------------------------------------------------
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
                                             ofNullable(solDay).map(v -> MonthDay.of(solMonth, v))
                                                     .map(Item.DAY_FORMATTER::format))
                        .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, ofNullable(pageNo))
                        .build())
                .retrieve()
                .bodyToMono(Response.class)
                ;
    }

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
    protected @NotNull Mono<Response> getSolCalInfo(@NotNull final Year lunYear, @NotNull final Month lunMonth,
                                                    @Max(30) @Min(1) @Nullable final Integer lunDay,
                                                    @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> b.pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                        .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                        .queryParam(QUERY_PARAM_NAME_LUN_YEAR, Item.YEAR_FORMATTER.format(lunYear))
                        .queryParam(QUERY_PARAM_NAME_LUN_MONTH, Item.MONTH_FORMATTER.format(lunMonth))
                        .queryParamIfPresent(QUERY_PARAM_NAME_LUN_DAY,
                                             ofNullable(lunDay).map(v -> MonthDay.of(lunMonth, v))
                                                     .map(Item.DAY_FORMATTER::format))
                        .queryParamIfPresent(QUERY_PARAM_NAME_PAGE_NO, ofNullable(pageNo))
                        .build())
                .retrieve()
                .bodyToMono(Response.class);
    }

    public Flux<Item> getSolCalInfo(@NotNull final LocalDate lunarDate) {
        final Year lunYear = Year.from(lunarDate);
        final Month lunMonth = Month.from(lunarDate);
        final int lunDay = lunarDate.getDayOfMonth();
        final AtomicInteger pageNo = new AtomicInteger();
        return getSolCalInfo(lunYear, lunMonth, lunDay, pageNo.incrementAndGet())
                .expand(r -> {
                    if (r.getBody().isLastPage()) {
                        return Mono.empty();
                    }
                    return getSolCalInfo(lunYear, lunMonth, lunDay, pageNo.incrementAndGet());
                })
                .flatMap(r -> Flux.fromIterable(r.getBody().getItems()));
    }

    public Flux<Item> getSolCalInfo(@NotNull final YearMonth lunarYearMonth) {
        final Year lunYear = Year.from(lunarYearMonth);
        final Month lunMonth = Month.from(lunarYearMonth);
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
                .bodyToMono(Response.class);
    }

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
