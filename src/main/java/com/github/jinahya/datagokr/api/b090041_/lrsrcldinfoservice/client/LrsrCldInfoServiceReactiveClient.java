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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

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
import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * A component class for accessing {@link #BASE_URL}.
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
    @NotNull
    public Mono<Response> getLunCalInfo(@NotNull final Year year, @NotNull final Month month,
                                        @Nullable final Integer dayOfMonth, @Positive final Integer pageNo) {
        return webClient()
                .get()
                .uri(b -> b.pathSegment("getLunCalInfo")
                        .queryParam(QUERY_PARAM_NAME_SERVICE_KEY, serviceKey())
                        .queryParam("solYear", Response.Body.Item.YEAR_FORMATTER.format(year))
                        .queryParam("solMonth", Response.Body.Item.MONTH_FORMATTER.format(month))
                        .queryParamIfPresent("solDay", ofNullable(dayOfMonth)
                                .map(v -> MonthDay.of(month, v))
                                .map(Response.Body.Item.DAY_OF_MONTH_FORMATTER::format))
                        .queryParamIfPresent("pageNo", ofNullable(pageNo))
                        .build())
                .retrieve()
                .bodyToMono(Response.class);
    }

    @NotNull
    public Mono<Response.Body.Item> getLunCalInfo(@NotNull final LocalDate localDate) {
        return getLunCalInfo(Year.of(localDate.getYear()), localDate.getMonth(), localDate.getDayOfMonth(), null)
                .map(this::getItems)
                .handle((v, s) -> {
                    if (v.isEmpty()) {
                        s.error(new RuntimeException("empty items retrieved"));
                    } else {
                        assert v.size() == 1;
                        s.next(v.get(0));
                        s.complete();
                    }
                })
                ;
    }

    public void getLunCalInfo(@NotNull final Year year, @NotNull final Month month,
                              @NotNull final Sinks.Many<? super Response.Body.Item> sink) {
        for (int pageNo = 1; ; pageNo++) {
            final Response response = getLunCalInfo(year, month, null, pageNo).block();
            final List<Response.Body.Item> items;
            try {
                items =getItems(response);
            } catch (final RuntimeException re) {
                sink.emitError(re, (t, r) -> {
                    log.error("failed to emit error; type: {}, result: {}", t, r);
                    return false;
                });
                return;
            }
            items.forEach(item -> {
                sink.emitNext(item, (t, r) -> {
                    log.error("failed to emit next; type: {}, result: {}", t, r);
                    return false;
                });
            });
            if (items.isEmpty()) {
                break;
            }
        }
        sink.emitComplete((t, r) -> {
            log.error("failed to emit complete; type: {}, result: {}", t, r);
            return false;
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Autowired
    @LrsrCldInfoServiceWebClient
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    private WebClient webClient;
}
