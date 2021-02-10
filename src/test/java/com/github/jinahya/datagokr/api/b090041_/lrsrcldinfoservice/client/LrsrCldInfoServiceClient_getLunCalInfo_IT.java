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

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarDate)")
    @Test
    void getLunCalInfo_Expected_SolarDate() {
        final LocalDate solarDate = LocalDate.now();
        assertThat(clientInstance().getLunCalInfo(solarDate))
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getSolarYear()).isNotNull().isEqualTo(Year.from(solarDate));
                    assertThat(i.getSolarMonth()).isNotNull().isSameAs(solarDate.getMonth());
                    assertThat(i.getSolarDayOfMonth()).isNotNull().isEqualTo(solarDate.getDayOfMonth());
                    assertThat(i.getSolarDayOfWeek()).isNotNull().isEqualTo(solarDate.getDayOfWeek());
                    assertThat(i.getSolarLeapYear()).isNotNull().isEqualTo(solarDate.isLeapYear());
                    assertThat(i.getSolarJulianDay()).isNotNull().isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
                })
        ;
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarYearMonth)")
    @Test
    void getLunCalInfo_Expected_SolarYearMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getLunCalInfo(solarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getSolarYear()).isNotNull().isEqualTo(Year.from(solarYearMonth));
            assertThat(i.getSolarMonth()).isNotNull().isSameAs(solarYearMonth.getMonth());
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, executor, collection)")
    @Test
    void getLunCalInfo_Expected_SolarYear() {
        final List<Item> items = new ArrayList<>();
        final Year solarYear = Year.now();
        clientInstance().getLunCalInfo(solarYear, commonPool(), items);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getSolarYear()).isNotNull().isEqualTo(solarYear);
        });
    }
}
