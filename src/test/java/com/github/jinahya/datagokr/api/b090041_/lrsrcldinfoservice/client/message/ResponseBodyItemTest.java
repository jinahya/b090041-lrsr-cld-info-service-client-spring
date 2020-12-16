package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.JulianFields;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResponseBodyItemTest {

    @Test
    void test_getSolYearMonthDayAsLocalDate() {
        final Response.Body.Item item = new Response.Body.Item();
        item.setSolYearMonthDayAsLocalDate(null);
        final LocalDate expected = LocalDate.now();
        item.setSolYearMonthDayAsLocalDate(expected);
        assertThat(item.getSolYearMonthDayAsLocalDate()).isNotNull().isEqualTo(expected);
        assertThat(item.getSolYear()).isNotNull().isEqualTo(Response.Body.Item.YEAR_FORMATTER.format(expected));
        assertThat(item.getSolMonth()).isNotNull().isEqualTo(Response.Body.Item.MONTH_FORMATTER.format(expected));
        assertThat(item.getSolDay()).isNotNull().isEqualTo(Response.Body.Item.DAY_FORMATTER.format(expected));
        assertThat(item.getSolWeek()).isNotNull().isEqualTo(Response.Body.Item.WEEK_FORMATTER.format(expected));
        assertThat(item.getSolLeapyear()).isNotNull()
                .isEqualTo(expected.isLeapYear() ? Response.Body.Item.LEAP : Response.Body.Item.NORMAL);
        assertThat(item.getSolJd()).isNotNull().isEqualTo(expected.getLong(JulianFields.JULIAN_DAY));
    }

    @Test
    void test_getLunYearMonthDayAsLocalDate() {
        final Response.Body.Item item = new Response.Body.Item();
        item.setLunYearMonthDayAsLocalDate(null);
        final LocalDate expected = LocalDate.now();
        item.setLunYearMonthDayAsLocalDate(expected);
        assertThat(item.getLunYearMonthDayAsLocalDate()).isNotNull().isEqualTo(expected);
        assertThat(item.getLunYear()).isNotNull().isEqualTo(Response.Body.Item.YEAR_FORMATTER.format(expected));
        assertThat(item.getLunMonth()).isNotNull().isEqualTo(Response.Body.Item.MONTH_FORMATTER.format(expected));
    }
}