package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class Item_Json_Test {

    @MethodSource({"com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ItemTest#items"})
    @ParameterizedTest
    void _Jackson_(final Item expected) throws Exception {
        // not available in 2.1.18.RELEASE
//        final ObjectMapper mapper = JsonMapper.builder()
//                .addModule(new ParameterNamesModule())
//                .addModule(new Jdk8Module())
//                .addModule(new JavaTimeModule())
//                .build();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModules(new ParameterNamesModule(), new Jdk8Module(), new JavaTimeModule());
        final String string = mapper.writeValueAsString(expected);
        final Item actual = mapper.readValue(string, Item.class);
        assertThat(actual).isEqualTo(expected);
    }
}