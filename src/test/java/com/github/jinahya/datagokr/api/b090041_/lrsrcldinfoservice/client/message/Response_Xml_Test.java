package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class Response_Xml_Test {

    private static Stream<Response> responses() {
        return ResponseResources.responses();
    }

    @MethodSource({"responses"})
    @ParameterizedTest
    void _Jaxb_(final Response expected) throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(Response.class);
        final Marshaller marshaller = context.createMarshaller();
        final StringWriter writer = new StringWriter();
        marshaller.marshal(expected, writer);
        final Response actual = context.createUnmarshaller()
                .unmarshal(new StreamSource(new StringReader(writer.toString())), Response.class)
                .getValue();
        assertThat(actual).isEqualTo(expected);
    }
}