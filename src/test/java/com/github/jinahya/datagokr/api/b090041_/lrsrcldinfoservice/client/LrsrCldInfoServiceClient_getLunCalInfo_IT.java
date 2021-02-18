package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, month, dayOfMonth)")
    @Test
    void getLunCalInfo_Expected_YearMonthDay() {
        final LocalDate now = LocalDate.now();
        final Year year = Year.from(now);
        final Month month = now.getMonth();
        final int dayOfMonth = now.getDayOfMonth();
        assertThat(clientInstance().getLunCalInfo(year, month, dayOfMonth))
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(year);
                    assertThat(i.getSolMonth()).isNotNull().isSameAs(month);
                    assertThat(i.getSolDay()).isNotNull().isEqualTo(dayOfMonth);
                    assertThat(i.getSolLeapyear()).isNotNull().isEqualTo(now.isLeapYear());
                    assertThat(i.getSolWeek()).isNotNull().isEqualTo(now.getDayOfWeek());
                    assertThat(i.getSolJd()).isNotNull().isEqualTo(now.getLong(JulianFields.JULIAN_DAY));
                })
        ;
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, month, null)")
    @Test
    void getLunCalInfo_Expected_YearMonth() {
        final YearMonth now = YearMonth.now();
        final Year year = Year.from(now);
        final Month month = now.getMonth();
        final List<Item> items = clientInstance().getLunCalInfo(year, month, null);
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(year);
                    assertThat(i.getSolMonth()).isNotNull().isSameAs(month);
                });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getLunCalInfo(year, executor, collection)")
    @Test
    void getLunCalInfo_Expected_Year() {
        final Year year = Year.now();
        final List<Item> items = clientInstance().getLunCalInfo(year, commonPool(), new ArrayList<>());
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(year);
                });
    }
}
