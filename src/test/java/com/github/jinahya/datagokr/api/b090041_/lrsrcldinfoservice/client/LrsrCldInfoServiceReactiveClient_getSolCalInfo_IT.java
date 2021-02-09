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
import static java.util.Optional.ofNullable;
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
        final String lunYearExpected = Item.YEAR_FORMATTER.format(lunarDate);
        final String lunMonthExpected = Item.MONTH_FORMATTER.format(lunarDate);
        final String lunDayExpected = ofNullable(lunDay).map(Item::formatDay).orElse(null);
        clientInstance().getSolCalInfo(Year.from(lunarDate), Month.from(lunarDate), lunDay, null)
                .doOnNext(r -> {
                    assertThat(r.getBody().getItems()).isNotEmpty().doesNotContainNull().allSatisfy(i -> {
                        assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYearExpected);
                        assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonthExpected);
                        if (lunDay != null) {
                            assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDayExpected);
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
        final String lunYear = Item.YEAR_FORMATTER.format(lunarDate);
        final String lunMonth = Item.MONTH_FORMATTER.format(lunarDate);
        final String lunDay = Item.DAY_FORMATTER.format(lunarDate);
        clientInstance().getSolCalInfo(Year.from(lunarDate), Month.from(lunarDate), lunarDate.getDayOfMonth())
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYearMonth)")
    @Test
    void getSolCalInfo_Expected_LunarYearMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final String lunYearExpected = Item.YEAR_FORMATTER.format(lunarYearMonth);
        final String lunMonthExpected = Item.MONTH_FORMATTER.format(lunarYearMonth);
        clientInstance().getSolCalInfo(lunarYearMonth)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYearExpected);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonthExpected);
                })
                .blockLast();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(year)")
    @Test
    void getSolCalInfo_Expected_Year() {
        final Year lunarYear = Year.now();
        final String expectedLunYear = Item.YEAR_FORMATTER.format(lunarYear);
        final Map<String, List<String>> map
                = clientInstance().getSolCalInfo(lunarYear, Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(expectedLunYear);
                })
                .<Map<String, List<String>>>collect(
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
