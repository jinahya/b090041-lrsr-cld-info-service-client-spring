package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class SingleKoreanDayOfWeekAdapterTest extends TemporalStringAdapterTest<SingleKoreanDayOfWeekAdapter, DayOfWeek> {

    SingleKoreanDayOfWeekAdapterTest() {
        super(SingleKoreanDayOfWeekAdapter.class, DayOfWeek.class);
    }

    @Test
    void marshal__() throws Exception {
        assertThat(adapterInstance().marshal(DayOfWeek.MONDAY)).isNotBlank().isEqualTo("월");
        assertThat(adapterInstance().marshal(DayOfWeek.TUESDAY)).isNotBlank().isEqualTo("화");
        assertThat(adapterInstance().marshal(DayOfWeek.WEDNESDAY)).isNotBlank().isEqualTo("수");
        assertThat(adapterInstance().marshal(DayOfWeek.THURSDAY)).isNotBlank().isEqualTo("목");
        assertThat(adapterInstance().marshal(DayOfWeek.FRIDAY)).isNotBlank().isEqualTo("금");
        assertThat(adapterInstance().marshal(DayOfWeek.SATURDAY)).isNotBlank().isEqualTo("토");
        assertThat(adapterInstance().marshal(DayOfWeek.SUNDAY)).isNotBlank().isEqualTo("일");
    }

    @Test
    void unmarshal__() throws Exception {
        assertThat(adapterInstance().unmarshal("월")).isNotNull().isSameAs(DayOfWeek.MONDAY);
        assertThat(adapterInstance().unmarshal("화")).isNotNull().isSameAs(DayOfWeek.TUESDAY);
        assertThat(adapterInstance().unmarshal("수")).isNotNull().isSameAs(DayOfWeek.WEDNESDAY);
        assertThat(adapterInstance().unmarshal("목")).isNotNull().isSameAs(DayOfWeek.THURSDAY);
        assertThat(adapterInstance().unmarshal("금")).isNotNull().isSameAs(DayOfWeek.FRIDAY);
        assertThat(adapterInstance().unmarshal("토")).isNotNull().isSameAs(DayOfWeek.SATURDAY);
        assertThat(adapterInstance().unmarshal("일")).isNotNull().isSameAs(DayOfWeek.SUNDAY);
    }
}