package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

class UuuuYearAdapterTest extends TemporalStringAdapterTest<UuuuYearAdapter, Year> {

    UuuuYearAdapterTest() {
        super(UuuuYearAdapter.class, Year.class);
    }

    @Test
    void marshal__() throws Exception {
        final Year expected = Year.now();
        final Year actual = adapterInstance().unmarshal(expected.toString());
        assertThat(actual).isNotNull().isEqualTo(expected);
    }

    @Test
    void unmarshal__() throws Exception {
        final Year expected = Year.now();
        final Year actual = adapterInstance().unmarshal(expected.toString());
        assertThat(actual).isNotNull().isEqualTo(expected);
    }
}