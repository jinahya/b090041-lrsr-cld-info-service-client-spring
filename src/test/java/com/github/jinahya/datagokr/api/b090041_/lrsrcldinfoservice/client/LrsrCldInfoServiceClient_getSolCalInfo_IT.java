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

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunYear, lunMonth, lunDay)")
    @Test
    void getSolCalInfo_Expected_YearMonthDay() {
        final LocalDate now = LocalDate.now().withDayOfMonth(1);
        final Year lunYear = Year.from(now);
        final Month lunMonth = now.getMonth();
        final int lunDay = now.getDayOfMonth();
        final List<Item> items = clientInstance().getSolCalInfo(lunYear, lunMonth, lunDay);
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunYear, lunMonth, null)")
    @Test
    void getSolCalInfo_Expected_YearMonth() {
        final YearMonth now = YearMonth.now();
        final Year lunYear = Year.from(now);
        final Month lunMonth = now.getMonth();
        final List<Item> items = clientInstance().getSolCalInfo(lunYear, lunMonth, null);
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(year, executor, collection)")
    @Test
    void getSolCalInfo_Expected_Year() {
        final Year year = Year.now();
        final List<Item> items = clientInstance().getSolCalInfo(year, commonPool(), new ArrayList<>());
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(year);
                });
    }
}
