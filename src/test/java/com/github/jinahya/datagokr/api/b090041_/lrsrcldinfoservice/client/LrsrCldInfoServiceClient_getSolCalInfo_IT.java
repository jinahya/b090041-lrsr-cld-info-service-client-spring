package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getSolCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getSolCalInfo_with_localDate() {
        final LocalDate lunarDate = LocalDate.now();
        final Response.Body.Item item = clientInstance().getSolCalInfo(lunarDate);
        assertThat(item).isNotNull().satisfies(i -> {
            final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarDate);
            final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarDate);
            final String lunDay = Response.Body.Item.DAY_FORMATTER.format(lunarDate);
            assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
            assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
            assertThat(i.getLunarDate()).isNotNull().isEqualTo(lunarDate);
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getLunCalInfo_with_yearMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final List<Response.Body.Item> items = new ArrayList<>();
        final int count = clientInstance().getSolCalInfo(lunarYearMonth, i -> {
            final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarYearMonth);
            final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarYearMonth);
            assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
            items.add(i);
        });
        assertThat(count).isEqualTo(items.size());
    }
}