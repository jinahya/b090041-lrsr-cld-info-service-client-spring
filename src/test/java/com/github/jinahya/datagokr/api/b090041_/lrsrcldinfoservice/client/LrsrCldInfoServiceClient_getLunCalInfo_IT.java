package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response.Body.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.JulianFields;
import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solYear, solMonth, solDay, pageNo)")
    @Test
    void getLunCalInfo_() {
        final LocalDate solarDate = LocalDate.now();
        final Integer solDay = current().nextBoolean() ? solarDate.getDayOfMonth() : null;
        final Response response = clientInstance()
                .getLunCalInfo(Year.from(solarDate), Month.from(solarDate), solDay, null)
                .getBody();
        final String solYearExpected = Item.YEAR_FORMATTER.format(solarDate);
        final String solMonthExpected = Item.MONTH_FORMATTER.format(solarDate);
        final String solDayExpected = Item.DAY_FORMATTER.format(solarDate);
        assertThat(response).isNotNull().satisfies(r -> {
            assertThat(r.getHeader().isResultCodeSuccess()).isTrue();
            assertThat(r.getBody().getItems()).allSatisfy(i -> {
                assertThat(i.getSolYear()).isNotNull().isEqualTo(solYearExpected);
                assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonthExpected);
                assertThat(i.getSolarLeapYear()).isEqualTo(solarDate.isLeapYear());
                if (solDay != null) {
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(solDayExpected);
                    final long julianDay = solarDate.getLong(JulianFields.JULIAN_DAY);
                    assertThat(i.getSolJd()).isNotNull().isEqualTo(julianDay);
                    assertThat(i.getSolarJulianDay()).isNotNull().isEqualTo(julianDay);
                }
            });
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarDate)")
    @Test
    void getLunCalInfo_Expected_SolarDate() {
        final LocalDate solarDate = LocalDate.now();
        final String solYear = Item.YEAR_FORMATTER.format(solarDate);
        final String solMonth = Item.MONTH_FORMATTER.format(solarDate);
        final String solDay = Item.DAY_FORMATTER.format(solarDate);
        assertThat(clientInstance().getLunCalInfo(solarDate)).isNotNull().isNotEmpty().allSatisfy(i -> {
            assertThat(i).isNotNull();
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
            assertThat(i.getSolarLeapYear()).isEqualTo(solarDate.isLeapYear());
            assertThat(i.getSolJd()).isNotNull().isEqualTo(solarDate.getLong(JulianFields.JULIAN_DAY));
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(solarYearMonth)")
    @Test
    void getLunCalInfo_Expected_SolarMonth() {
        final YearMonth solarYearMonth = YearMonth.now();
        final List<Item> items = clientInstance().getLunCalInfo(solarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String solYear = Item.YEAR_FORMATTER.format(solarYearMonth);
            final String solMonth = Item.MONTH_FORMATTER.format(solarYearMonth);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
        });
    }
}
