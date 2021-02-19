package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.DayOfWeek;

@Slf4j
public class SolWeekSerializer extends StdSerializer<DayOfWeek> {

    public SolWeekSerializer() {
        super(DayOfWeek.class);
        adapter = new SolWeekWeekAdapter();
    }

    @Override
    public void serialize(final DayOfWeek value, final JsonGenerator generator, final SerializerProvider provider)
            throws IOException {
        if (value == null) {
            generator.writeNull();
            return;
        }
        final String marshalled;
        try {
            marshalled = adapter.marshal(value);
        } catch (final Exception e) {
            throw new RuntimeException("failed to marshal " + value, e);
        }
        generator.writeString(marshalled);
    }

    private final SolWeekWeekAdapter adapter;
}
