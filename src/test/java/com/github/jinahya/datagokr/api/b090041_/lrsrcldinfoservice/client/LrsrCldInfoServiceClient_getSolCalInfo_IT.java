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
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getSolCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunYear, lunMonth, lunDay, pageNo)")
    @Test
    //@SuppressWarnings("java:S5841")
    void getSolCalInfo_() {
        final LocalDate lunarDate = LocalDate.now().withDayOfMonth(1);
        final Integer lunDay = current().nextBoolean() ? lunarDate.getDayOfMonth() : null;
        final Response response = clientInstance().getResponse(
                clientInstance().getSolCalInfo(Year.from(lunarDate), Month.from(lunarDate), lunDay, null));
        final String lunYearExpected = Response.Body.Item.YEAR_FORMATTER.format(lunarDate);
        final String lunMonthExpected = Response.Body.Item.MONTH_FORMATTER.format(lunarDate);
        final String lunDayExpected = ofNullable(lunDay).map(Response.Body.Item::formatDay).orElse(null);
        assertThat(response).isNotNull().satisfies(r -> {
            assertThat(response.getBody().getItems()).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
                assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYearExpected);
                assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonthExpected);
                if (lunDay != null) {
                    assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDayExpected);
                }
            });
        });
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYear, lunarMonth, lunarDayOfMonth)")
    @Test
    @SuppressWarnings("java:S5841")
    void getSolCalInfo_Expected_LunarDate() {
        final LocalDate lunarDate = LocalDate.now().withDayOfMonth(1);
        final List<Response.Body.Item> items = clientInstance().getSolCalInfo(
                Year.from(lunarDate), Month.from(lunarDate), lunarDate.getDayOfMonth());
        final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarDate);
        final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarDate);
        final String lunDay = Response.Body.Item.DAY_FORMATTER.format(lunarDate);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
            assertThat(i.getLunDay()).isNotNull().isEqualTo(lunDay);
            assertThat(i.getLunarYear()).isNotNull().isEqualTo(Year.of(lunarDate.getYear()));
            assertThat(i.getLunarYear()).isNotNull().isEqualTo(Year.of(lunarDate.getYear()));
            assertThat(i.getLunarMonth()).isNotNull().isEqualTo(lunarDate.getMonth());
            assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(lunarDate.getDayOfMonth());
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSolCalInfo(lunarYearMonth)")
    @Test
    void getSolCalInfo_Expected_LunarMonth() {
        final YearMonth lunarYearMonth = YearMonth.now();
        final List<Response.Body.Item> items = clientInstance().getSolCalInfo(lunarYearMonth);
        assertThat(items).isNotNull().isNotEmpty().doesNotContainNull().allSatisfy(i -> {
            final String lunYear = Response.Body.Item.YEAR_FORMATTER.format(lunarYearMonth);
            final String lunMonth = Response.Body.Item.MONTH_FORMATTER.format(lunarYearMonth);
            assertThat(i.getLunYear()).isNotNull().isEqualTo(lunYear);
            assertThat(i.getLunMonth()).isNotNull().isEqualTo(lunMonth);
        });
    }
}
