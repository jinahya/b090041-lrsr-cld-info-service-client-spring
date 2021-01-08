package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.file.Files.readAllLines;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ResponseTest {

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

    public static List<Response> responses() {
        return RESPONSES;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @MethodSource({"responses"})
    @ParameterizedTest
    void testJaxb(final Response expected) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(Response.class);
        final Marshaller marshaller = context.createMarshaller();
        final StringWriter writer = new StringWriter();
        marshaller.marshal(expected, writer);
        final String xml = writer.toString();
        final StringReader reader = new StringReader(xml);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final Response actual = (Response) unmarshaller.unmarshal(reader);
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"responses"})
    @ParameterizedTest
    void testJsonb(final Response expected) {
        final Jsonb jsonb = JsonbBuilder.create();
        final String json = jsonb.toJson(expected);
        final Response actual = jsonb.fromJson(json, Response.class);
        assertThat(actual).isEqualTo(expected);
    }

    @MethodSource({"responses"})
    @ParameterizedTest
    void testJackson(final Response expected) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String string = mapper.writeValueAsString(expected);
        final Response actual = mapper.readValue(string, Response.class);
        assertThat(actual).isEqualTo(expected);
    }
}