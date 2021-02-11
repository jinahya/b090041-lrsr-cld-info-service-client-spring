package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSpcifyLunCalInfo(fromSolarYear,toSolarYear,lunarMonth,lunarDayOfMonth,lunarLeapMonth)")
    @Test
    void getSpcifyLunCalInfo() {
        final Item item = clientInstance().getLunCalInfo(LocalDate.now().withDayOfMonth(1)).blockLast();
        assertThat(item).isNotNull();
        final LocalDate solarDate = item.getSolarDate();
        final Year solarYear = Year.from(solarDate);
        final Year fromSolarYear = solarYear.minusYears(10L);
        final Year toSolarYear = solarYear.plusYears(5L);
        final Month lunarMonth = item.getLunMonth();
        final int lunarDayOfMonth = item.getLunDay();
        final boolean lunarLeapMonth = item.getLunLeapmonth();
        assertThat(item).isNotNull();
        clientInstance().getSpcifyLunCalInfo(fromSolarYear, toSolarYear, lunarMonth, lunarDayOfMonth, lunarLeapMonth)
                .doOnNext(i -> {
                    assertThat(i.getLunMonth()).isNotNull().isSameAs(lunarMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunarDayOfMonth);
                    assertThat(i.getLunLeapmonth()).isNotNull().isEqualTo(lunarLeapMonth);
                })
                .blockLast();
    }
}
