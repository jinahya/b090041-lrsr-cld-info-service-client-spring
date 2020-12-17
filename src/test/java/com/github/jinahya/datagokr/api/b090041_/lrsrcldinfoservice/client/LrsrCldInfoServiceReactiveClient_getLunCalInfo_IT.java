package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import reactor.core.publisher.Sinks;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.JulianFields;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @Test
    void test() {
        log.debug("serviceKey: {}", System.getProperty(SYSTEM_PROPERTY_SERVICE_KEY));
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void test_getLunCalInfo_with_solarDate() {
        final LocalDate solarDate = LocalDate.now();
        final Sinks.One<Response.Body.Item> sinksOne = Sinks.one();
        clientInstance().getLunCalInfo(
                solarDate,
                sinksOne,
                (t, r) -> {
                    log.error("failed to emit value; type: {}, result: {}", t, r);
                    return false;
                },
                (t, r) -> {
                    log.error("failed to emit error; type: {}, result: {}", t, r);
                    return false;
                }
        );
        final Response.Body.Item item = sinksOne.asMono().block();
        assertThat(item).isNotNull().satisfies(i -> {
            final String solYear = Response.Body.Item.YEAR_FORMATTER.format(solarDate);
            final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(solarDate);
            final String solDay = Response.Body.Item.DAY_FORMATTER.format(solarDate);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
            assertThat(i.getSolLeapyearAsBoolean()).isEqualTo(solarDate.isLeapYear());
            assertThat(i.getSolJd()).isNotNull().isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void test_getLunCalInfo_with_solarYearMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final Sinks.Many<Response.Body.Item> sinksMany = Sinks.many().unicast().onBackpressureBuffer();
        clientInstance().getLunCalInfo(
                solarYearMonth,
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
                });
        final String solYear = Response.Body.Item.YEAR_FORMATTER.format(solarYearMonth);
        final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(solarYearMonth);
        sinksMany.asFlux()
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
                })
                .blockLast();
    }
}
