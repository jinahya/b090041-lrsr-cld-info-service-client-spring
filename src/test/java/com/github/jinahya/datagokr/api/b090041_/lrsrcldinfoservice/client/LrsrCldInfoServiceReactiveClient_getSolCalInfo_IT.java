package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import reactor.core.publisher.Sinks;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSolCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getSolCalInfo_with_localDate() {
        final LocalDate lunarDate = LocalDate.now();
        final Sinks.Many<Response.Body.Item> sinksMany = Sinks.many().unicast().onBackpressureBuffer();
        clientInstance().getSolCalInfo(
                lunarDate,
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
                    log.error("failed to emit complet; type: {}, result: {}", t, r);
                    return false;
                }
        );
        final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarDate);
        final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarDate);
        final String lunDay = Response.Body.Item.DAY_FORMATTER.format(lunarDate);
        sinksMany.asFlux()
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getSolCalInfo_with_yearMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final Sinks.Many<Response.Body.Item> sinksMany = Sinks.many().unicast().onBackpressureBuffer();
        clientInstance().getSolCalInfo(
                lunarYearMonth,
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
                    log.error("failed to emit cmplete; type: {}, result: {}", t, r);
                    return false;
                });
        final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarYearMonth);
        final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarYearMonth);
        sinksMany.asFlux()
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                })
                .blockLast();
    }
}
