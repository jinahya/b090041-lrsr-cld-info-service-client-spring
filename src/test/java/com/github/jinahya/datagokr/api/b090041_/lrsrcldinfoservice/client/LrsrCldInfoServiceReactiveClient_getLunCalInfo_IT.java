package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.JulianFields;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solYear, solMonth, solDay, pageNo)")
    @Test
    void getLunCalInfo_() {
        final LocalDate solarDate = LocalDate.now();
        final Integer solDay = current().nextBoolean() ? solarDate.getDayOfMonth() : null;
        clientInstance().getLunCalInfo(Year.from(solarDate), Month.from(solarDate), solDay, null)
                .doOnNext(r -> {
                    final String solYearExpected = Response.Body.Item.YEAR_FORMATTER.format(solarDate);
                    final String solMonthExpected = Response.Body.Item.MONTH_FORMATTER.format(solarDate);
                    final String solDayExpected = Response.Body.Item.DAY_FORMATTER.format(solarDate);
                    assertThat(r.getBody().getItems()).isNotEmpty().allSatisfy(i -> {
                        assertThat(i.getSolYear()).isNotNull().isEqualTo(solYearExpected);
                        assertThat(i.getSolarLeapYear()).isEqualTo(solarDate.isLeapYear());
                        assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonthExpected);
                        if (solDay != null) {
                            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDayExpected);
                            final Long julianDay = solarDate.getLong(JulianFields.JULIAN_DAY);
                            assertThat(i.getSolJd()).isNotNull().isEqualTo(julianDay);
                            assertThat(i.getSolarJulianDay()).isNotNull().isEqualTo(julianDay);
                        }
                    });
                })
                .block();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarDate)")
    @Test
    void getLunCalInfo_Expected_SolarDate() {
        final LocalDate now = LocalDate.now();
        final String solYear = Response.Body.Item.YEAR_FORMATTER.format(now);
        final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(now);
        final String solDay = Response.Body.Item.DAY_FORMATTER.format(now);
        clientInstance().getLunCalInfo(now)
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
                    assertThat(i.getSolarLeapYear()).isEqualTo(now.isLeapYear());
                    assertThat(i.getSolJd()).isNotNull().isEqualTo(now.getLong(JulianFields.JULIAN_DAY));
                })
                .blockLast();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarYearMonth)")
    @Test
    void getLunCalInfo_Expected_SolarMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final String solYear = Response.Body.Item.YEAR_FORMATTER.format(solarYearMonth);
        final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(solarYearMonth);
        clientInstance().getLunCalInfo(YearMonth.from(solarYearMonth))
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
                    assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
                })
                .blockLast();
    }
}
