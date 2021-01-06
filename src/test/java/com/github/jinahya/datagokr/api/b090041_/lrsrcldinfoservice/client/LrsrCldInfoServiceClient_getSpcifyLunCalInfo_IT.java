package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response.Body.Item;
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

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSpcifyLunCalInfo(Year, Year, Month, int, boolean)")
    @Test
    void getSpcifyLunCalInfo() {
        final Item item = clientInstance()
                .getLunCalInfo(LocalDate.now())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("failed to getLunCalInfo"));
        final Year fromSolYear = item.getSolarYear().minusYears(1L);
        final Year toSolYear = item.getSolarYear().plusYears(1L);
        final Month lunMonth = item.getLunarMonth();
        final int lunDay = item.getLunarDayOfMonth();
        final boolean leapMonth = item.getLunarLeapMonth();
        final List<Item> items = clientInstance().getSpcifyLunCalInfo(
                fromSolYear, toSolYear, lunMonth, lunDay, leapMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLunarMonth()).isNotNull().isEqualTo(lunMonth);
            assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(lunDay);
        });
    }
}
