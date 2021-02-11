package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import java.time.Year;
import java.time.format.DateTimeFormatter;

public class UuuuYearAdapter extends FormattedTemporalAdapter<Year> {

    public static final DateTimeFormatter YEAR_UUUU_FORMATTER = DateTimeFormatter.ofPattern("uuuu");

    public UuuuYearAdapter() {
        super(Year.class, YEAR_UUUU_FORMATTER, Year::from);
    }
}
