package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A class for static fields/methods of {@link Item}.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
class Item_Static_Test {

    /**
     * Returns an item for {@code 2023-02-29(lunar/leap)} for {@code 2023-03-20(solar)}.
     *
     * @return an item for {@code 2023-02-29(lunar/leap)} for {@code 2023-03-20(solar)}.
     * @see #itemOf20230229Leap()
     */
    static Item itemOf20230229Normal() {
        final Item instance = new Item();
        instance.setLunYear(Year.of(2023));
        instance.setLunMonth(Month.FEBRUARY);
        instance.setLunDay(29);
        instance.setLunLeapmonth(Boolean.FALSE);
        instance.setSolarDate(LocalDate.of(2023, Month.MARCH, 20));
        return instance;
    }

    /**
     * Returns an item for {@code 2023-02-29(lunar/normal)} for {@code 2023-04-19(solar)}.
     *
     * @return an item for {@code 2023-02-29(lunar/normal)} for {@code 2023-04-19(solar)}.
     * @see #itemOf20230229Normal()
     */
    static Item itemOf20230229Leap() {
        final Item instance = new Item();
        instance.setLunYear(Year.of(2023));
        instance.setLunMonth(Month.FEBRUARY);
        instance.setLunDay(29);
        instance.setLunLeapmonth(Boolean.TRUE);
        instance.setSolarDate(LocalDate.of(2023, Month.APRIL, 19));
        return instance;
    }

    /**
     * Assert {@link Item#COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST} works as expected.
     */
    @Test
    void COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST_WorksAsExpected_() {
        final Item leap = itemOf20230229Leap();
        final Item norm = itemOf20230229Normal();
        final List<Item> items = Arrays.asList(norm, leap);
        items.sort(Item.COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST);
        assertThat(items.get(0)).isEqualTo(leap);
        assertThat(items.get(1)).isEqualTo(norm);
    }

    /**
     * Assert {@link Item#COMPARING_LUNAR_DATE_LEAP_MONTH_LAST} works as expected.
     */
    @Test
    void COMPARING_LUNAR_DATE_LEAP_MONTH_LAST_WorksAsExpected_() {
        final Item leap = itemOf20230229Leap();
        final Item norm = itemOf20230229Normal();
        final List<Item> items = Arrays.asList(leap, norm);
        items.sort(Item.COMPARING_LUNAR_DATE_LEAP_MONTH_LAST);
        assertThat(items.get(0)).isEqualTo(norm);
        assertThat(items.get(1)).isEqualTo(leap);
    }

    Item_Static_Test() {
        super();
    }
}