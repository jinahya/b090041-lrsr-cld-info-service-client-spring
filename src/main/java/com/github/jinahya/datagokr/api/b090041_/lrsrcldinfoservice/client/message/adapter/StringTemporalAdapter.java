package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.temporal.TemporalAccessor;

import static java.util.Objects.requireNonNull;

public abstract class StringTemporalAdapter<T extends TemporalAccessor> extends XmlAdapter<String, T> {

    protected StringTemporalAdapter(final Class<T> clazz) {
        super();
        this.clazz = requireNonNull(clazz, "clazz is null");
    }

    protected final Class<T> clazz;
}
