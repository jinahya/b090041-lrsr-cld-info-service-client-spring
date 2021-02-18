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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.naturalOrder;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, month, dayOfMonth)")
    @Test
    void getLunCalInfo_Expected_YearMonthDay() {
        final LocalDate now = LocalDate.now();
        final Year year = Year.from(now);
        final Month month = now.getMonth();
        final int dayOfMonth = now.getDayOfMonth();
        clientInstance().getLunCalInfo(year, month, dayOfMonth)
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(year);
                    assertThat(i.getSolMonth()).isNotNull().isSameAs(month);
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(dayOfMonth);
                    assertThat(i.getSolLeapyear()).isNotNull().isEqualTo(now.isLeapYear());
                    assertThat(i.getSolWeek()).isNotNull().isEqualTo(now.getDayOfWeek());
                    assertThat(i.getSolJd()).isNotNull().isEqualTo(now.getLong(JulianFields.JULIAN_DAY));
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, month, null)")
    @Test
    void getLunCalInfo_Expected_YearMonth() {
        final YearMonth now = YearMonth.now();
        final Year year = Year.from(now);
        final Month month = now.getMonth();
        clientInstance().getLunCalInfo(year, month, null)
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(year);
                    assertThat(i.getSolMonth()).isNotNull().isSameAs(month);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, parallelism, scheduler)")
    @Test
    void getLunCalInfo_Expected_Year() {
        final Year year = Year.now();
        final int parallelism = Runtime.getRuntime().availableProcessors();
        final Map<Month, List<Integer>> lunarMonthsAndDays
                = clientInstance().getLunCalInfo(year, parallelism, Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(Year.from(year));
                })
                .<Map<Month, List<Integer>>>collect(
                        () -> new EnumMap<>(Month.class),
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
