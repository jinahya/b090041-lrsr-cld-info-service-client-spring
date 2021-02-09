package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import static java.lang.Integer.parseInt;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSpcifyLunCalInfo(fromSolarYear,toSolarYear,lunarMonth,lunarDayOfMonth,lunarLeapMonth)")
    @Test
    void getSpcifyLunCalInfo() {
        final Item item = clientInstance().getLunCalInfo(LocalDate.now().withDayOfMonth(1)).blockLast();
        assertThat(item).isNotNull();
        final Year solarYear = Year.parse(item.getSolYear(), Item.YEAR_FORMATTER);
        final Year fromSolarYear = solarYear.minusYears(10L);
        final Year toSolarYear = solarYear.plusYears(5L);
        final Month lunarMonth = Month.of(parseInt(item.getLunMonth()));
        final int lunarDayOfMonth = parseInt(item.getLunDay());
        final boolean lunarLeapMonth = item.getLunarLeapMonth();
        assertThat(item).isNotNull();
        clientInstance().getSpcifyLunCalInfo(fromSolarYear, toSolarYear, lunarMonth, lunarDayOfMonth, lunarLeapMonth)
                .doOnNext(i -> {
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(item.getLunMonth());
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(item.getLunDay());
                    assertThat(i.getLunLeapmonth()).isNotNull().isEqualTo(item.getLunLeapmonth());
                })
                .blockLast();
    }
}
