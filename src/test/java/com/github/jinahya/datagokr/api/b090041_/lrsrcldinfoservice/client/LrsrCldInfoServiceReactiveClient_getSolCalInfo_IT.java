package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSolCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void getSolCalInfo_Expected_LocalDateNow() {
        final LocalDate lunarDate = LocalDate.now();
        final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarDate);
        final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarDate);
        final String lunDay = Response.Body.Item.DAY_FORMATTER.format(lunarDate);
        clientInstance().getSolCalInfo(lunarDate)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void getSolCalInfo_Expected_YearMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarYearMonth);
        final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarYearMonth);
        clientInstance().getSolCalInfo(lunarYearMonth)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                })
                .blockLast();
    }
}
