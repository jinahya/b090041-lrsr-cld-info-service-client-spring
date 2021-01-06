package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.JulianFields;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(LocalDate)")
    @Test
    void getLunCalInfo_Expected_YearMonthDay() {
        final LocalDate now = LocalDate.now();
        final String solYear = Response.Body.Item.YEAR_FORMATTER.format(now);
        final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(now);
        final String solDay = Response.Body.Item.DAY_FORMATTER.format(now);
        clientInstance().getLunCalInfo(now)
                .doOnNext(i -> {
                    log.debug("item: {}", i);
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
                    assertThat(i.getSolarLeapYear()).isEqualTo(now.isLeapYear());
                    assertThat(i.getSolJd()).isNotNull().isEqualTo(now.getLong(JulianFields.JULIAN_DAY));
                })
                .block();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void getLunCalInfo_Expected_YearMonth() {
        final YearMonth now = YearMonth.now();
        final String solYear = Response.Body.Item.YEAR_FORMATTER.format(now);
        final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(now);
        clientInstance().getLunCalInfo(now)
                .doOnNext(i -> {
                    log.debug("item: {}", i);
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
                })
                .blockLast();
    }
}
