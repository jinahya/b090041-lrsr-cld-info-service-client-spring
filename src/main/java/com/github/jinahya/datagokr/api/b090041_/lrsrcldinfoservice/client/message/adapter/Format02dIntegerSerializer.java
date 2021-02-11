package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class Format02dIntegerSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(final Integer value, final JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        try {
            gen.writeRawValue(adapter.marshal(value));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final Format02dIntegerAdapter adapter = new Format02dIntegerAdapter();
}
