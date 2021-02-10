package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.Month;
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

    static Item i20230229l() {
        return Item.builder()
                .lunYear(2023)
                .lunMonth("02")
                .lunDay("29")
                .lunLeapmonth(Item.LEAP)
                .solYear(2023)
                .solMonth("04")
                .solDay("19")
                .build()
                .solarDate(LocalDate.of(2023, Month.APRIL, 19));
    }

    static Item i20230229n() {
        return Item.builder()
                .lunYear(2023)
                .lunMonth("02")
                .lunDay("29")
                .lunLeapmonth(Item.NON_LEAP)
                .solYear(2023)
                .solMonth("04")
                .solDay("19")
                .build()
                .solarDate(LocalDate.of(2023, Month.MARCH, 20));
    }

    /**
     * Assert {@link Item#COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST} works as expected.
     */
    @Test
    void COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST_WorksAsExpected_() {
        final Item leap = i20230229l();
        final Item norm = i20230229n();
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
        final Item leap = i20230229l();
        final Item norm = i20230229n();
        final List<Item> items = Arrays.asList(leap, norm);
        items.sort(Item.COMPARING_LUNAR_DATE_LEAP_MONTH_LAST);
        assertThat(items.get(0)).isEqualTo(norm);
        assertThat(items.get(1)).isEqualTo(leap);
    }

    public Item_Static_Test() {
        super();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private final Validator validator;
}