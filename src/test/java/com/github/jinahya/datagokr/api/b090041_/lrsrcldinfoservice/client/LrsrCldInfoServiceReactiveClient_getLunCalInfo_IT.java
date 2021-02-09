package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
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
        final String expectedSolYear = Item.YEAR_FORMATTER.format(solarDate);
        final String expectedSolMonth = Item.MONTH_FORMATTER.format(solarDate);
        final String expectedSolDay = Item.DAY_FORMATTER.format(solarDate);
        clientInstance().getLunCalInfo(solarDate)
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(expectedSolYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(expectedSolMonth);
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(expectedSolDay);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarYearMonth)")
    @Test
    void getLunCalInfo_Expected_SolarYearMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final String expectedSolYear = Item.YEAR_FORMATTER.format(solarYearMonth);
        final String expectedSolMonth = Item.MONTH_FORMATTER.format(solarYearMonth);
        clientInstance().getLunCalInfo(YearMonth.from(solarYearMonth))
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(expectedSolYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(expectedSolMonth);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year)")
    @Test
    void getLunCalInfo_Expected_Year() {
        final Year solarYear = Year.now();
        final String expectedSolYear = Item.YEAR_FORMATTER.format(solarYear);
        final Map<String, List<String>> lunarMonthsAndDays
                = clientInstance().getLunCalInfo(solarYear, Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(expectedSolYear);
                })
                .<Map<String, List<String>>>collect(
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
