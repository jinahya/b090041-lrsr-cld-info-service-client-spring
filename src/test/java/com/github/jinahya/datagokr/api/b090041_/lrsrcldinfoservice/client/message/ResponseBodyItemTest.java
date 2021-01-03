package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.JulianFields;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResponseBodyItemTest {

    @Test
    void testSetSolarDate() {
        final Response.Body.Item item = new Response.Body.Item();
        {
            item.setSolarDate(null);
            assertThat(item.getSolarYear()).isNull();
            assertThat(item.getSolarMonth()).isNull();
            assertThat(item.getSolarDayOfMonth()).isNull();
            assertThat(item.getSolarLeapYear()).isNull();
            assertThat(item.getSolarDayOfWeek()).isNull();
            assertThat(item.getSolarJulianDay()).isNull();
        }
        final LocalDate solarDate = LocalDate.now();
        item.setSolarDate(solarDate);
        assertThat(item.getSolarDate()).isNotNull().isEqualTo(solarDate);
        assertThat(item.getSolYear()).isNotNull().isEqualTo(Response.Body.Item.YEAR_FORMATTER.format(solarDate));
        assertThat(item.getSolMonth()).isNotNull().isEqualTo(Response.Body.Item.MONTH_FORMATTER.format(solarDate));
        assertThat(item.getSolDay()).isNotNull().isEqualTo(Response.Body.Item.DAY_FORMATTER.format(solarDate));
        assertThat(item.getSolWeek()).isNotNull().isEqualTo(Response.Body.Item.WEEK_FORMATTER.format(solarDate));
        assertThat(item.getSolLeapyear()).isNotNull()
                .isEqualTo(solarDate.isLeapYear() ? Response.Body.Item.LEAP : Response.Body.Item.NORMAL);
        assertThat(item.getSolJd()).isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
    }
}