package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.naturalOrder;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSolCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunYear, lunMonth, lunDay, pageNo")
    @Test
    void getSolCalInfo_() {
        final LocalDate lunarDate = LocalDate.now().withDayOfMonth(1);
        final Integer lunDay = current().nextBoolean() ? lunarDate.getDayOfMonth() : null;
        clientInstance().getSolCalInfo(Year.from(lunarDate), Month.from(lunarDate), lunDay, null)
                .doOnNext(r -> {
                    assertThat(r.getBody().getItems()).isNotEmpty().doesNotContainNull().allSatisfy(i -> {
                        assertThat(i.getLunYear()).isNotNull().isEqualTo(Year.from(lunarDate));
                        assertThat(i.getLunMonth()).isNotNull().isSameAs(Month.from(lunarDate));
                        if (lunDay != null) {
                            assertThat(i.getLunDay()).isNotNull().isEqualTo(lunarDate.getDayOfMonth());
                        }
                    });
                })
                .block();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYear,lunarMonth,lunarDayOfMonth")
    @Test
    void getSolCalInfo_Expected_LunarDate() {
        final LocalDate lunarDate = LocalDate.now().withDayOfMonth(1);
        clientInstance().getSolCalInfo(Year.from(lunarDate), lunarDate.getMonth(), lunarDate.getDayOfMonth())
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(Year.from(lunarDate));
                    assertThat(i.getLunMonth()).isNotNull().isSameAs(lunarDate.getMonth());
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunarDate.getDayOfMonth());
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYearMonth)")
    @Test
    void getSolCalInfo_Expected_LunarYearMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final int lunYearExpected = lunarYearMonth.getYear();
        final String lunMonthExpected = Item.MONTH_FORMATTER.format(lunarYearMonth);
        clientInstance().getSolCalInfo(lunarYearMonth)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(Year.from(lunarYearMonth));
                    assertThat(i.getLunMonth()).isNotNull().isSameAs(lunarYearMonth.getMonth());
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(year)")
    @Test
    void getSolCalInfo_Expected_Year() {
        final Year lunarYear = Year.now();
        final Map<Month, List<Integer>> map
                = clientInstance().getSolCalInfo(lunarYear, Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunarYear);
                })
                .<Map<Month, List<Integer>>>collect(
                        TreeMap::new,
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
