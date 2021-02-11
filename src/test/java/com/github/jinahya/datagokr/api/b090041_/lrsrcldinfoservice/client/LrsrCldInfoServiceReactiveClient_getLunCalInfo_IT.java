package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.naturalOrder;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarDate)")
    @Test
    void getLunCalInfo_Expected_SolarDate() {
        final LocalDate solarDate = LocalDate.now();
        clientInstance().getLunCalInfo(solarDate)
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(Year.from(solarDate));
                    assertThat(i.getSolMonth()).isNotNull().isSameAs(solarDate.getMonth());
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(solarDate.getDayOfMonth());
                    assertThat(i.getSolLeapyear()).isNotNull().isEqualTo(solarDate.isLeapYear());
                    assertThat(i.getSolWeek()).isNotNull().isEqualTo(solarDate.getDayOfWeek());
                    assertThat(i.getSolarJulianDay()).isNotNull().isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarYearMonth)")
    @Test
    void getLunCalInfo_Expected_SolarYearMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        clientInstance().getLunCalInfo(YearMonth.from(solarYearMonth))
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(Year.from(solarYearMonth));
                    assertThat(i.getSolMonth()).isNotNull().isSameAs(solarYearMonth.getMonth());
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year)")
    @Test
    void getLunCalInfo_Expected_Year() {
        final Year solarYear = Year.now();
        final Map<Month, List<Integer>> lunarMonthsAndDays
                = clientInstance().getLunCalInfo(solarYear, Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(Year.from(solarYear));
                })
                .<Map<Month, List<Integer>>>collect(
                        TreeMap::new,
                        (m, i) -> m.compute(i.getLunMonth(), (k, v) -> v == null ? new ArrayList<>() : v)
                                .add(i.getLunDay()))
                .block();
        assert lunarMonthsAndDays != null;
        lunarMonthsAndDays.forEach((m, l) -> {
            l.sort(naturalOrder());
            log.debug("{}: {}", m, l);
        });
    }
}
