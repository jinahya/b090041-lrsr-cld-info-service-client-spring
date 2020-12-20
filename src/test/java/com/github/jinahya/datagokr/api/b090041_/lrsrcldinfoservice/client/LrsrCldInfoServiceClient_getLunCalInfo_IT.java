package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response.Body.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.JulianFields;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(LocalDate)")
    @Test
    void verify_getLunCalInfo_with_localDate() {
        final LocalDate solarDate = LocalDate.now();
        final List<Item> items = clientInstance().getLunCalInfo(solarDate);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String solYear = Item.YEAR_FORMATTER.format(solarDate);
            final String solMonth = Item.MONTH_FORMATTER.format(solarDate);
            final String solDay = Item.DAY_FORMATTER.format(solarDate);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
            assertThat(i.getSolarLeapYear()).isEqualTo(solarDate.isLeapYear());
            assertThat(i.getSolJd()).isNotNull().isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(YearMonth)")
    @Test
    void verify_getLunCalInfo_with_yearMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getLunCalInfo(solarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String solYear = Item.YEAR_FORMATTER.format(solarYearMonth);
            final String solMonth = Item.MONTH_FORMATTER.format(solarYearMonth);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
        });
    }
}
