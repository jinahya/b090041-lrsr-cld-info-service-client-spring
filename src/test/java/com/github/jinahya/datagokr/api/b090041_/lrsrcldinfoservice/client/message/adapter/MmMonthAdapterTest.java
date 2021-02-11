package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

class MmMonthAdapterTest extends TemporalStringAdapterTest<MmMonthAdapter, Month> {

    MmMonthAdapterTest() {
        super(MmMonthAdapter.class, Month.class);
    }

    @EnumSource(Month.class)
    @ParameterizedTest
    void marshal__(final Month expected) throws Exception {
        final String marshal = adapterInstance().marshal(expected);
        assertThat(marshal).isNotBlank().isEqualTo(String.format("%1$02d", expected.getValue()));
    }

    @EnumSource(Month.class)
    @ParameterizedTest
    void unmarshal__(final Month expected) throws Exception {
        final Month actual = adapterInstance().unmarshal(String.format("%1$02d", expected.getValue()));
        assertThat(actual).isNotNull().isSameAs(expected);
    }
}