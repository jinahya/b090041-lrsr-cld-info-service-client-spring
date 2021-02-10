package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSpcifyLunCalInfo(fromSolarYear, toSolarYear, lunarMonth, lunarDayOfMonth, lunarLeapMonth)")
    @Test
    void getSpcifyLunCalInfo_() {
        final Item item = clientInstance().getLunCalInfo(LocalDate.now().withDayOfMonth(1)).get(0);
        final Year solarYear = item.getSolarYear();
        final Year fromSolarYear = solarYear.minusYears(10L);
        final Year toSolarYear = solarYear.plusYears(5L);
        final Month lunarMonth = item.getLunarMonth();
        final int lunarDayOfMonth = item.getLunarDayOfMonth();
        final boolean lunarLeapMonth = item.getLunarLeapMonth();
        final List<Item> items = clientInstance().getSpcifyLunCalInfo(
                fromSolarYear, toSolarYear, lunarMonth, lunarDayOfMonth, lunarLeapMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLunarMonth()).isNotNull().isEqualTo(lunarMonth);
            assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(lunarDayOfMonth);
            assertThat(i.getLunarLeapMonth()).isNotNull().isEqualTo(lunarLeapMonth);
        });
    }
}
