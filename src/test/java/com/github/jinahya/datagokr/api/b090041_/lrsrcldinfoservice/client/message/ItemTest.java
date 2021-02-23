package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.temporal.JulianFields;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ItemTest {

    private static Stream<Item> items() {
        return ResponseResources.items();
    }

    ItemTest() {
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

    @ParameterizedTest
    @MethodSource({"items"})
    void getGanzhiOfYearKore_Valid_(final Item item) {
        log.debug("ganzhiYearKore: {}", item.getGanzhiYearKore());
        assertThat(item.getGanzhiYearKore()).isNotBlank().hasSize(2);
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void getGanzhiOfYearHans_Valid_(final Item item) {
        log.debug("ganzhiYearHans: {}", item.getGanzhiYearHans());
        assertThat(item.getGanzhiYearHans()).isNotBlank().hasSize(2);
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void getGanzhiOfMonthKore_Valid_(final Item item) {
        final String ganzhiOfMonthKore = item.getGanzhiMonthKore();
        log.debug("ganzhiMonthKore: {}", ganzhiOfMonthKore);
        if (ganzhiOfMonthKore != null) {
            assertThat(item.getGanzhiMonthKore()).hasSize(2);
        }
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void getGanzhiOfMonthHans_Valid_(final Item item) {
        final String ganzhiMonthHans = item.getGanzhiMonthHans();
        log.debug("ganzhiMonthHans: {}", ganzhiMonthHans);
        if (ganzhiMonthHans != null) {
            assertThat(item.getGanzhiMonthHans()).hasSize(2);
        }
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void getGanzhiOfDayOfMonthKore_Valid_(final Item item) {
        log.debug("ganzhiDayKore: {}", item.getGanzhiDayKore());
        assertThat(item.getGanzhiDayKore()).isNotBlank().hasSize(2);
    }

    @ParameterizedTest
    @MethodSource({"items"})
    void getGanzhiOfDayOfMonthHans_Valid_(final Item item) {
        log.debug("ganzhiDayHans: {}", item.getGanzhiDayHans());
        assertThat(item.getGanzhiDayHans()).isNotBlank().hasSize(2);
    }

    private final Validator validator;
}