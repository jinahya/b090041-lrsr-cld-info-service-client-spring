package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import org.junit.platform.commons.util.ReflectionUtils;

import java.time.temporal.TemporalAccessor;

import static java.util.Objects.requireNonNull;

abstract class TemporalStringAdapterTest<T extends StringTemporalAdapter<U>, U extends TemporalAccessor> {

    protected TemporalStringAdapterTest(final Class<T> adapterClass, final Class<U> temporalClass) {
        super();
        this.adapterClass = requireNonNull(adapterClass, "adapterClass is null");
        this.temporalClass = requireNonNull(temporalClass, "temporalClass is null");
    }

    protected T adapterInstance() {
        return ReflectionUtils.newInstance(adapterClass);
    }

    protected final Class<T> adapterClass;

    protected final Class<U> temporalClass;
}