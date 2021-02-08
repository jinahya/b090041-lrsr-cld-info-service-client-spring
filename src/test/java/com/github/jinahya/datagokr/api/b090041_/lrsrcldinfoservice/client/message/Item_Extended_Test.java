package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Month;
import java.time.Year;
import java.util.stream.Stream;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ResponseTest.responses;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class Item_Extended_Test {

    // -----------------------------------------------------------------------------------------------------------------
    @ParameterizedTest
    @MethodSource({"com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ItemTest#items"})
    void getLunarYear__(final Item item) {
        assertThat(item.getLunarYear())
                .isNotNull()
                .isEqualTo(Year.of(Integer.parseInt(item.getLunYear())));
    }

    @ParameterizedTest
    @MethodSource({"com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ItemTest#items"})
    void getLunarMonth__(final Item item) {
        assertThat(item.getLunarMonth())
                .isNotNull()
                .isEqualTo(Month.of(Integer.parseInt(item.getLunMonth())));
    }

    @ParameterizedTest
    @MethodSource({"com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ItemTest#items"})
    void getLunarDayOfMonth__(final Item item) {
        assertThat(item.getLunarDayOfMonth())
                .isNotNull()
                .isEqualTo(Integer.parseInt(item.getLunDay()));
    }

    @ParameterizedTest
    @MethodSource({"com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ItemTest#items"})
    void getLunarLeapMonth__(final Item item) {
        assertThat(item.getLunarLeapMonth())
                .isNotNull()
                .isEqualTo(Boolean.valueOf(Item.LEAP.equals(item.getLunLeapmonth())));
    }
}