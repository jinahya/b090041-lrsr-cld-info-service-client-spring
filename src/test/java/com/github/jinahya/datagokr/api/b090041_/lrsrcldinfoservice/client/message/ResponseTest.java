package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResponseTest {

    private static Stream<Response> responses() {
        return ResponseResources.responses();
    }

    ResponseTest() {
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