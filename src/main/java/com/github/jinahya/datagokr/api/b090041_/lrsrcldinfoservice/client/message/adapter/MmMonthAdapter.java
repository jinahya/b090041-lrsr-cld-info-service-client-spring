package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import java.time.Month;
import java.time.format.DateTimeFormatter;

public class MmMonthAdapter extends FormattedTemporalAdapter<Month> {

    public static final DateTimeFormatter MONTH_MM_FORMATTER = DateTimeFormatter.ofPattern("MM");

    public MmMonthAdapter() {
        super(Month.class, MONTH_MM_FORMATTER, Month::from);
    }
}
