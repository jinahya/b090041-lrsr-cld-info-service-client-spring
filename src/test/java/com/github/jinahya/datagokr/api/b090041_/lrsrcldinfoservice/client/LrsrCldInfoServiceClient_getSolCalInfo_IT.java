package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getSolCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(LocalDate)")
    @Test
    @SuppressWarnings("java:S5841")
    void getSolCalInfo_Expected_LocalDateNow() {
        final LocalDate now = LocalDate.now();
        final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(now);
        final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(now);
        final String lunDay = Response.Body.Item.DAY_FORMATTER.format(now);
        final List<Response.Body.Item> items
                = clientInstance().getSolCalInfo(Year.from(now), Month.from(now), now.getDayOfMonth());
        assertThat(items).isNotNull()
//                .isNotEmpty() // may be empty!!!
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
                    assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
                    assertThat(i.getLunarYear()).isNotNull().isEqualTo(Year.of(now.getYear()));
                    assertThat(i.getLunarYear()).isNotNull().isEqualTo(Year.of(now.getYear()));
                    assertThat(i.getLunarMonth()).isNotNull().isEqualTo(now.getMonth());
                    assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(now.getDayOfMonth());
                })
        ;
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(YearMonth)")
    @Test
    void getSolCalInfo_Expected_YearMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final List<Response.Body.Item> items = clientInstance().getSolCalInfo(lunarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarYearMonth);
            final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarYearMonth);
            assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
        });
    }
}
