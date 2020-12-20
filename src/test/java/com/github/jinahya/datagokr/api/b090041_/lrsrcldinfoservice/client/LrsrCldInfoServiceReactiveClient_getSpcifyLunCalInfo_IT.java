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
        final Response.Body.Item item = clientInstance().getLunCalInfo(LocalDate.now()).block();
        assert item != null;
        final Year fromSolYear = item.getSolarYear().minusYears(1L);
        final Year toSolYear = item.getSolarYear().plusYears(1L);
        final Month lunMonth = item.getLunarMonth();
        final int lunDay = item.getLunarDayOfMonth();
        final boolean leapMonth = item.getLunarLeapMonth();
        final Sinks.Many<Response.Body.Item> sinksMany = Sinks.many().unicast().onBackpressureBuffer();
        clientInstance().getSpcifyLunCalInfo(
                fromSolYear,
                toSolYear,
                lunMonth,
                lunDay,
                leapMonth,
                sinksMany,
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
        sinksMany.asFlux()
                .doOnNext(i -> {
                    assertThat(i.getLunarMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }
}
