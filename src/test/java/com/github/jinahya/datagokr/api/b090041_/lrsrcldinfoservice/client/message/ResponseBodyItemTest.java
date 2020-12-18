package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.JulianFields;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResponseBodyItemTest {

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void compile_PATTERN_REGEXP_간지() {
        Pattern.compile(Response.Body.Item.PATTERN_REGEXP_간지);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void compile_PATTERN_REGEXP_干支() {
        Pattern.compile(Response.Body.Item.PATTERN_REGEXP_干支);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void test_PATTERN_REGEXP_간지_干支() {
        final Pattern pattern = Pattern.compile(Response.Body.Item.PATTERN_REGEXP_간지_干支);
        for (final String input : new String[] {"정축(丁丑)", "기해(己亥)", "갑술(甲戌)"}) {
            final Matcher matcher = pattern.matcher(input);
            final boolean matches = matcher.matches();
            assertThat(matches).isTrue();
        }
    }

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

    @Test
    void testSetLunarDate() {
        final Response.Body.Item item = new Response.Body.Item();
        {
            item.setLunarDate(null);
            assertThat(item.getLunarYear()).isNull();
            assertThat(item.getLunarMonth()).isNull();
            assertThat(item.getLunarDayOfMonth()).isNull();
        }
        final LocalDate expected = LocalDate.now();
        item.setLunarDate(expected);
        assertThat(item.getLunarDate()).isNotNull().isEqualTo(expected);
        assertThat(item.getLunYear()).isNotNull().isEqualTo(Response.Body.Item.YEAR_FORMATTER.format(expected));
        assertThat(item.getLunMonth()).isNotNull().isEqualTo(Response.Body.Item.MONTH_FORMATTER.format(expected));
    }
}