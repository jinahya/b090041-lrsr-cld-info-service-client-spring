package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.readAllLines;
import static java.util.stream.Collectors.toList;
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
            final Path path = Paths.get(ResponseTest.class.getResource("index.txt").toURI());
            final List<String> names = readAllLines(path).stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .filter(s -> !s.startsWith("#"))
                    .collect(toList());
            for (String name : names) {
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

    public ResponseTest() {
        super();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @MethodSource({"responses"})
    void _Valid_(final Response response) {
        assertThat(validator.validate(response)).isEmpty();
    }

    private final Validator validator;
}