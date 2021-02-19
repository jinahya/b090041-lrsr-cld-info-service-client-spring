package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SolWeekWeekAdapter extends FormattedTemporalAdapter<DayOfWeek> {

    public static final DateTimeFormatter SINGLE_KOREAN_DAY_OF_WEEK_FORMATTER
            = DateTimeFormatter.ofPattern("E", Locale.KOREAN);

    public SolWeekWeekAdapter() {
        super(DayOfWeek.class, SINGLE_KOREAN_DAY_OF_WEEK_FORMATTER, DayOfWeek::from);
    }
}
