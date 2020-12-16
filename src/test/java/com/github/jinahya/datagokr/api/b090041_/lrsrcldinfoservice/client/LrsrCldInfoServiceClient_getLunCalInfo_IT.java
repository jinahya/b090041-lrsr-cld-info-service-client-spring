package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getLunCalInfo_with_localDate() {
        final LocalDate localDate = LocalDate.now();
        final Response.Body.Item item = clientInstance().getLunCalInfo(localDate);
        assertThat(item).isNotNull().satisfies(i -> {
            final String solYear = Response.Body.Item.YEAR_FORMATTER.format(localDate);
            final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(localDate);
            final String solDay = Response.Body.Item.DAY_FORMATTER.format(localDate);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
            assertThat(i.getSolLeapyearAsBoolean()).isEqualTo(localDate.isLeapYear());
            assertThat(i.getSolJd()).isNotNull().isEqualTo(localDate.getLong(JulianFields.JULIAN_DAY));
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getLunCalInfo_with_yearMonth() {
        final YearMonth yearMonth = YearMonth.now();
        final List<Response.Body.Item> items = new ArrayList<>();
        final int count = clientInstance().getLunCalInfo(yearMonth, i -> {
            final String solYear = Response.Body.Item.YEAR_FORMATTER.format(yearMonth);
            final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(yearMonth);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            items.add(i);
        });
        assertThat(count).isEqualTo(items.size());
    }
}
