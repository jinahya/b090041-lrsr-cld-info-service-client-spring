package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Responses;
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
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Month;
import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MAX_DAY_OF_MONTH_LUNAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MAX_DAY_OF_MONTH_SOLAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MIN_DAY_OF_MONTH_LUNAR;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.MIN_DAY_OF_MONTH_SOLAR;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Flux.fromIterable;

/**
 * A client implementation uses an instance of {@link WebClient}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 * @see LrsrCldInfoServiceClient
 */
@Lazy
@Component
@Slf4j
public class LrsrCldInfoServiceReactiveClient extends AbstractLrsrCldInfoServiceClient {

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
    protected static Mono<Response> handled(final Mono<Response> mono) {
        return requireNonNull(mono, "mono is null").handle((r, h) -> {
            if (!Responses.isResultSuccessful(r)) {
                h.error(new WebClientException("unsuccessful result: " + r.getHeader()) {
                });
            } else {
                h.next(r);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public LrsrCldInfoServiceReactiveClient() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    private void onPostConstruct() {
        if (webClient == null) {
            log.warn("no web client autowired. using a bare instance...");
            webClient = WebClient.builder()
                    .baseUrl(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
                    .build();
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
     * @return a mono of response.
     */
    public @NotNull Mono<Response> getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(MAX_DAY_OF_MONTH_SOLAR) @Min(MIN_DAY_OF_MONTH_SOLAR) @Nullable final Integer solDay,
            @Positive @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_LUN_CAL_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_SOL_YEAR, solYear.getValue())
                            .queryParam(QUERY_PARAM_NAME_SOL_MONTH, MONTH_FORMATTER.format(solMonth));
                    ofNullable(solDay)
                            .map(AbstractLrsrCldInfoServiceClient::format02d)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_SOL_DAY, v));
                    ofNullable(pageNo).ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(LrsrCldInfoServiceReactiveClient::handled);
    }

    /**
     * Reads all responses from all pages of {@code /getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}; {@code null} for a whole month.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> getLunCalInfoForAllPages(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(MAX_DAY_OF_MONTH_SOLAR) @Min(MIN_DAY_OF_MONTH_SOLAR) @Nullable final Integer solDay) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getLunCalInfo(solYear, solMonth, solDay, pageNo.incrementAndGet())
                .expand(r -> {
                    if (Responses.isLastPage(r)) {
                        return Mono.empty();
                    }
                    return getLunCalInfo(solYear, solMonth, solDay, pageNo.incrementAndGet());
                });
    }

    /**
     * Reads all items from {@code /.../getLunCalInfo} with specified arguments.
     *
     * @param solYear  a value for {@link #QUERY_PARAM_NAME_SOL_YEAR ?solYear}.
     * @param solMonth a value for {@link #QUERY_PARAM_NAME_SOL_MONTH ?solMonth}.
     * @param solDay   a value for {@link #QUERY_PARAM_NAME_SOL_DAY ?solDay}; {@code null} for a whole month.
     * @return a flux of items.
     * @see #getLunCalInfo(Year, Month, Integer, Integer)
     */
    public @NotNull Flux<Item> getLunCalInfo(
            @NotNull final Year solYear, @NotNull final Month solMonth,
            @Max(MAX_DAY_OF_MONTH_SOLAR) @Min(MIN_DAY_OF_MONTH_SOLAR) @Nullable final Integer solDay) {
        return getLunCalInfoForAllPages(solYear, solMonth, solDay)
                .flatMap(r -> fromIterable(r.getBody().getItems()));
    }

    /**
     * Reads all items in specified solar year.
     *
     * @param year        the solar year whose all items are retrieved.
     * @param parallelism a value for parallelism.
     * @param scheduler   a scheduler.
     * @return a flux of items.
     * @see #getLunCalInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> getLunCalInfo(@NotNull final Year year, @Positive final int parallelism,
                                             @NotNull final Scheduler scheduler) {
        return Flux.fromArray(Month.values())
                .parallel(parallelism)
                .runOn(scheduler)
                .flatMap(m -> getLunCalInfo(year, m, null))
                .sequential();
    }

    // -------------------------------------------------------------------------------------------------- /getSolCalInfo

    /**
     * Retrieves a response from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}; {@code null} for a whole month.
     * @param pageNo   a value for {@link #QUERY_PARAM_NAME_PAGE_NO ?pageNo}; {@code null} for the first page.
     * @return a mono of response.
     */
    public @NotNull Mono<Response> getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay,
            @PositiveOrZero @Nullable final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> {
                    b.pathSegment(PATH_SEGMENT_GET_SOL_CAL_INFO)
                            .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                            .queryParam(QUERY_PARAM_NAME_LUN_YEAR, lunYear.getValue())
                            .queryParam(QUERY_PARAM_NAME_LUN_MONTH, MONTH_FORMATTER.format(lunMonth));
                    ofNullable(lunDay)
                            .map(AbstractLrsrCldInfoServiceClient::format02d)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_LUN_DAY, v));
                    ofNullable(pageNo)
                            .ifPresent(v -> b.queryParam(QUERY_PARAM_NAME_PAGE_NO, v));
                    return b.build();
                })
                .retrieve()
                .bodyToMono(Response.class)
                .as(LrsrCldInfoServiceReactiveClient::handled);
    }

    /**
     * Retrieves all responses from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}; {@code null} for a whole month.
     * @return a flux of responses.
     */
    public @NotNull Flux<Response> getSolCalInfoForAllPages(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay) {
        final AtomicInteger pageNo = new AtomicInteger();
        return getSolCalInfo(lunYear, lunMonth, lunDay, pageNo.incrementAndGet())
                .expand(r -> {
                    if (Responses.isLastPage(r)) {
                        return Mono.empty();
                    }
                    return getSolCalInfo(lunYear, lunMonth, lunDay, pageNo.incrementAndGet());
                });
    }

    /**
     * Retrieves all items from {@code /getSolCalInfo} with specified arguments.
     *
     * @param lunYear  a value for {@link #QUERY_PARAM_NAME_LUN_YEAR ?lunYear}.
     * @param lunMonth a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay   a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}; {@code null} for a whole month.
     * @return a flux of items.
     * @see #getSolCalInfo(Year, Month, Integer, Integer)
     */
    public Flux<Item> getSolCalInfo(
            @NotNull final Year lunYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) @Nullable final Integer lunDay) {
        return getSolCalInfoForAllPages(lunYear, lunMonth, lunDay)
                .flatMap(r -> fromIterable(r.getBody().getItems()));
    }

    /**
     * Retrieves all items for specified lunar year.
     *
     * @param year        the lunar year.
     * @param parallelism a value for parallelism.
     * @param scheduler   a scheduler.
     * @return a flux of all items in {@code year}.
     * @see #getSolCalInfo(Year, Month, Integer)
     */
    public @NotNull Flux<Item> getSolCalInfo(@NotNull final Year year, @Positive final int parallelism,
                                             @NotNull final Scheduler scheduler) {
        return Flux.fromArray(Month.values())
                .parallel(parallelism)
                .runOn(scheduler)
                .flatMap(m -> getSolCalInfo(year, m, null))
                .sequential();
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
     * @return a mono of response.
     */
    public Mono<Response> getSpcifyLunCalInfo(
            @NotNull final Year fromSolYear, @NotNull final Year toSolYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunDay, final boolean leapMonth,
            @PositiveOrZero final int pageNo) {
        if (toSolYear.isBefore(fromSolYear)) {
            throw new IllegalArgumentException(
                    "toSolYear(" + toSolYear + ") is before fromSolYear(" + fromSolYear + ")");
        }
        return webClient
                .get()
                .uri(b -> b
                        .pathSegment(PATH_SEGMENT_GET_SPCIFY_LUN_CAL_INFO)
                        .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                        .queryParam(QUERY_PARAM_NAME_FROM_SOL_YEAR, fromSolYear.getValue())
                        .queryParam(QUERY_PARAM_NAME_TO_SOL_YEAR, toSolYear.getValue())
                        .queryParam(QUERY_PARAM_NAME_LUN_MONTH, MONTH_FORMATTER.format(lunMonth))
                        .queryParam(QUERY_PARAM_NAME_LUN_DAY, format02d(lunDay))
                        .queryParam(QUERY_PARAM_NAME_LEAP_MONTH, queryParamValueLeapMonth(leapMonth))
                        .queryParam(QUERY_PARAM_NAME_PAGE_NO, pageNo)
                        .build()
                )
                .retrieve()
                .bodyToMono(Response.class)
                .as(LrsrCldInfoServiceReactiveClient::handled)
                ;
    }

    /**
     * Retrieves all responses from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a flux of responses.
     */
    public Flux<Response> getSpcifyLunCalInfoForAllPages(@NotNull final Year fromSolYear, @NotNull final Year toSolYear,
                                                         @NotNull final Month lunMonth, final int lunDay,
                                                         final boolean leapMonth) {
        final AtomicInteger pageNo = new AtomicInteger(0);
        return getSpcifyLunCalInfo(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth, pageNo.incrementAndGet())
                .expand(r -> {
                    if (Responses.isLastPage(r)) {
                        return Mono.empty();
                    }
                    return getSpcifyLunCalInfo(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth,
                                               pageNo.incrementAndGet());
                });
    }

    /**
     * Retrieves all items from {@code /getSpcifyLunCalInfo} with specified arguments.
     *
     * @param fromSolYear a value for {@link #QUERY_PARAM_NAME_FROM_SOL_YEAR ?fromSolYear}.
     * @param toSolYear   a value for {@link #QUERY_PARAM_NAME_TO_SOL_YEAR ?toSolYear}.
     * @param lunMonth    a value for {@link #QUERY_PARAM_NAME_LUN_MONTH ?lunMonth}.
     * @param lunDay      a value for {@link #QUERY_PARAM_NAME_LUN_DAY ?lunDay}.
     * @param leapMonth   a value for {@link #QUERY_PARAM_NAME_LEAP_MONTH ?leapMonth}.
     * @return a flux of items.
     * @see #getSpcifyLunCalInfo(Year, Year, Month, int, boolean, int)
     */
    public Flux<Item> getSpcifyLunCalInfo(
            @NotNull final Year fromSolYear, @NotNull final Year toSolYear, @NotNull final Month lunMonth,
            @Max(MAX_DAY_OF_MONTH_LUNAR) @Min(MIN_DAY_OF_MONTH_LUNAR) final int lunDay,
            final boolean leapMonth) {
        return getSpcifyLunCalInfoForAllPages(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth)
                .flatMap(r -> fromIterable(r.getBody().getItems()));
    }

    // ------------------------------------------------------------------------------------------------- instance fields
    @Autowired
    @LrsrCldInfoServiceWebClient
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private WebClient webClient;
}
