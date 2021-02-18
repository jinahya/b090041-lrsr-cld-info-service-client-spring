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
        final LocalDate date = LocalDate.now().withDayOfMonth(1);
        final Item item = clientInstance()
                .getLunCalInfo(Year.from(date), date.getMonth(), date.getDayOfMonth())
                .blockLast();
        assertThat(item).isNotNull();
        final LocalDate solarDate = item.getSolarDate();
        final Year solarYear = Year.from(solarDate);
        final Year fromSolYear = solarYear.minusYears(10L);
        final Year toSolYear = solarYear.plusYears(5L);
        final Month lunMonth = item.getLunMonth();
        final int lunDay = item.getLunDay();
        final boolean leapMonth = item.getLunLeapmonth();
        assertThat(item).isNotNull();
        clientInstance().getSpcifyLunCalInfo(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth)
                .doOnNext(i -> {
                    assertThat(i.getLunMonth()).isNotNull().isSameAs(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                    assertThat(i.getLunLeapmonth()).isNotNull().isEqualTo(leapMonth);
                })
                .blockLast();
    }
}
