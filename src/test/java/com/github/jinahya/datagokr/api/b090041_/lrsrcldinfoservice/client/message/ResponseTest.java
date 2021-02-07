package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResponseTest {

    private static final List<Response> RESPONSES;

    static Response unmarshal(final URL url) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(Response.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Response) unmarshaller.unmarshal(url);
    }

    static {
        final List<Response> responses = new ArrayList<>();
        try {
            for (final String name : readAllLines(Paths.get(ResponseTest.class.getResource("index.txt").toURI()))) {
                responses.add(unmarshal(ResponseTest.class.getResource(name)));
            }
        } catch (URISyntaxException | IOException | JAXBException e) {
            e.printStackTrace();
            throw new InstantiationError(e.getMessage());
        }
        RESPONSES = Collections.unmodifiableList(responses);
    }

    public static Stream<Response> responses() {
        return RESPONSES.stream();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"responses"})
    @ParameterizedTest
    void responses_Jackson(final Response expected) throws JsonProcessingException {
        final ObjectMapper mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build();
        final String string = mapper.writeValueAsString(expected);
        final Response actual = mapper.readValue(string, Response.class);
        assertThat(actual).isEqualTo(expected);
    }
}