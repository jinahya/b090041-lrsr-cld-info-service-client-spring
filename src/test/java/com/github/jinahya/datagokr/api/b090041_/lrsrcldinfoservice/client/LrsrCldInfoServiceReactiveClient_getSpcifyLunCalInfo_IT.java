package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import reactor.core.publisher.Sinks;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getSpcifyLunCalInfo() {
        final Response.Body.Item lunCalInfo = clientInstance().getLunCalInfo(LocalDate.now()).block();
        assert lunCalInfo != null;
        final LocalDate lunDate = lunCalInfo.getLunYearMonthDayAsLocalDate();
        final Year fromSolYear = Year.of(lunDate.getYear()).minusYears(1L);
        final Year toSolYear = Year.of(lunDate.getYear()).plusYears(1L);
        final Month lunMonth = lunDate.getMonth();
        final int lunDay = lunDate.getDayOfMonth();
        final boolean leapMonth = lunCalInfo.getLunLeapmonthAsBoolean();
        final Sinks.Many<Response.Body.Item> sink = Sinks.many().unicast().onBackpressureBuffer();
        clientInstance().getSpcifyLunCalInfo(
                fromSolYear,
                toSolYear,
                lunMonth,
                lunDay,
                leapMonth,
                sink,
                (t, r) -> {
                    log.error("failed to emit error; type: {}, result: {}", t, r);
                    return false;
                },
                (t, r) -> {
                    log.error("failed to emit next; type: {}, result: {}", t, r);
                    return false;
                },
                (t, r) -> {
                    log.error("failed to emit complete; type: {}, result: {}", t, r);
                    return false;
                });
        sink.asFlux()
                .doOnNext(i -> {
                    assertThat(i.getLunMonthAsMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDayAsDayOfMonth()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }
}
