package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.DayOfWeek;

@Slf4j
public class SolWeekDeserializer extends StdDeserializer<DayOfWeek> {

    public SolWeekDeserializer() {
        super(DayOfWeek.class);
        adapter = new SolWeekWeekAdapter();
    }

    @Override
    public DayOfWeek deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        final String value = parser.getValueAsString();
        try {
            return adapter.unmarshal(value);
        } catch (final Exception e) {
            throw new JsonParseException(parser, "failed to unmarshal " + value, e);
        }
    }

    private final SolWeekWeekAdapter adapter;
}
