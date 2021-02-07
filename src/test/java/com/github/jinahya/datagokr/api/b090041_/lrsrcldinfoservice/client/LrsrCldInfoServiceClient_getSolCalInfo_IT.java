package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getSolCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYear,lunarMonth,lunarDayOfMonth)")
    @Test
    @SuppressWarnings("java:S5841")
    void getSolCalInfo_Expected_LunarDate() {
        final LocalDate lunarDate = LocalDate.now().withDayOfMonth(1);
        final List<Item> items = clientInstance().getSolCalInfo(
                Year.from(lunarDate), Month.from(lunarDate), lunarDate.getDayOfMonth());
        final String expectedLunYear = Item.YEAR_FORMATTER.format(lunarDate);
        final String expectedLunMonth = Item.MONTH_FORMATTER.format(lunarDate);
        final String expectedLunDay = Item.DAY_FORMATTER.format(lunarDate);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLunYear()).isNotNull().isEqualTo(expectedLunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(expectedLunMonth);
            assertThat(i.getLunDay()).isNotNull().isEqualTo(expectedLunDay);
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYearMonth)")
    @Test
    void getSolCalInfo_Expected_LunarMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getSolCalInfo(lunarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String lunYear = Item.YEAR_FORMATTER.format(lunarYearMonth);
            final String lunMonth = Item.MONTH_FORMATTER.format(lunarYearMonth);
            assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(year, executor, collection)")
    @Test
    void getSolCalInfo_Expected_Year() {
        final List<Item> items = new ArrayList<>();
        final Year lunarYear = Year.now();
        final String expectedLunYear = Item.YEAR_FORMATTER.format(lunarYear);
        clientInstance().getSolCalInfo(lunarYear, commonPool(), items);
        items.sort(Item.COMPARING_IN_SOLAR);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLunYear()).isEqualTo(expectedLunYear);
        });
        log.debug("first: {}", items.get(0));
        log.debug("last: {}", items.get(items.size() - 1));
    }
}
