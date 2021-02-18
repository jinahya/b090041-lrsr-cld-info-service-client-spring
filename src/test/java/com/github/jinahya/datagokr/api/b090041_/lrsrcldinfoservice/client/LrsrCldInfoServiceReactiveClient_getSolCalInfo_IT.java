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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.naturalOrder;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSolCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunYear, lunMonth, lunDay")
    @Test
    void getSolCalInfo_Expected_YearMonthDay() {
        final LocalDate lunarDate = LocalDate.now().withDayOfMonth(1);
        final Year lunYear = Year.from(lunarDate);
        final Month lunMonth = lunarDate.getMonth();
        final int lunDay = lunarDate.getDayOfMonth();
        clientInstance().getSolCalInfo(lunYear, lunMonth, lunDay)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isSameAs(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunYear, lunMonth, null)")
    @Test
    void getSolCalInfo_Expected_YearMonth() {
        final YearMonth yearMonth = YearMonth.now();
        final Year lunYear = Year.from(yearMonth);
        final Month lunMonth = yearMonth.getMonth();
        clientInstance().getSolCalInfo(lunYear, lunMonth, null)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isSameAs(lunMonth);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(year, parallelism, scheduler)")
    @Test
    void getSolCalInfo_Expected_Year() {
        final Year year = Year.now();
        final int parallelism = Runtime.getRuntime().availableProcessors();
        final Map<Month, List<Integer>> map
                = clientInstance().getSolCalInfo(year, parallelism, Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(year);
                })
                .<Map<Month, List<Integer>>>collect(
                        () -> new EnumMap<>(Month.class),
                        (m, i) -> m.compute(i.getLunMonth(), (k, v) -> v == null ? new ArrayList<>() : v)
                                .add(i.getLunDay()))
                .block();
        assert map != null;
        map.forEach((m, l) -> {
            l.sort(naturalOrder());
            log.debug("{}: {}", m, l);
        });
    }
}
