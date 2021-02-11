package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class FormattedTemporalAdapter<T extends TemporalAccessor>
        extends StringTemporalAdapter<T> {

    protected FormattedTemporalAdapter(final Class<T> clazz, final DateTimeFormatter formatter,
                                       final Function<? super TemporalAccessor, ? extends T> converter) {
        super(clazz);
        this.formatter = requireNonNull(formatter, "formatter is null");
        this.converter = requireNonNull(converter, "converter is null");
    }

    @Override
    public T unmarshal(final String v) throws Exception {
        if (v == null) {
            return null;
        }
        return converter.apply(formatter.parse(v.trim()));
    }

    @Override
    public String marshal(final T v) throws Exception {
        if (v == null) {
            return null;
        }
        return formatter.format(v);
    }

    private final DateTimeFormatter formatter;

    private final Function<? super TemporalAccessor, ? extends T> converter;
}
