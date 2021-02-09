package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.temporal.JulianFields;
import java.util.stream.Stream;

import static com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.ResponseTest.responses;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ItemTest {

    static Stream<Item> items() {
        return responses().flatMap(r -> r.getBody().getItems().stream());
    }

    public ItemTest() {
        super();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void _Valid_(final Item item) {
        assertThat(validator.validate(item)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void getSolJd_Equals_SolarDateJulianDay(final Item item) {
        assertThat(item.getSolJd())
                .isNotNull()
                .isEqualTo(item.getSolarDate().getLong(JulianFields.JULIAN_DAY));
    }

    private final Validator validator;
}