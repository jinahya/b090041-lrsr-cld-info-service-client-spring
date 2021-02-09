package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.LEAP;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.NON_LEAP;
import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item.YEAR_FORMATTER;
import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarDate)")
    @Test
    void getLunCalInfo_Expected_SolarDate() {
        final LocalDate solarDate = LocalDate.now();
        final String solYear = Item.YEAR_FORMATTER.format(solarDate);
        final String solMonth = Item.MONTH_FORMATTER.format(solarDate);
        final String solDay = Item.DAY_FORMATTER.format(solarDate);
        assertThat(clientInstance().getLunCalInfo(solarDate)).isNotNull().isNotEmpty().allSatisfy(i -> {
            assertThat(i).isNotNull();
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
            assertThat(i.getSolLeapyear()).isEqualTo(solarDate.isLeapYear() ? LEAP : NON_LEAP);
            assertThat(i.getSolJd()).isNotNull().isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarYearMonth)")
    @Test
    void getLunCalInfo_Expected_SolarYearMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getLunCalInfo(solarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String solYear = Item.YEAR_FORMATTER.format(solarYearMonth);
            final String solMonth = Item.MONTH_FORMATTER.format(solarYearMonth);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, executor, collection)")
    @Test
    void getLunCalInfo_Expected_SolarYear() {
        final List<Item> items = new ArrayList<>();
        final Year solarYear = Year.now();
        final String expectedSolYear = YEAR_FORMATTER.format(solarYear);
        clientInstance().getLunCalInfo(solarYear, commonPool(), items);
        items.sort(Item.COMPARING_SOLAR_DATE);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getSolYear()).isEqualTo(expectedSolYear);
        });
        log.debug("first: {}", items.get(0));
        log.debug("last: {}", items.get(items.size() - 1));
    }
}
