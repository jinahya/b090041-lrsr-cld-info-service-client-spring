package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void verify_getSpcifyLunCalInfo() {
        final Response.Body.Item lunCalInfo = clientInstance().getLunCalInfo(LocalDate.now());
        final LocalDate lunDate = lunCalInfo.getLunYearMonthDayAsLocalDate();
        final Year fromSolYear = Year.of(lunDate.getYear()).minusYears(1L);
        final Year toSolYear = Year.of(lunDate.getYear()).plusYears(1L);
        final Month lunMonth = lunDate.getMonth();
        final int lunDay = lunDate.getDayOfMonth();
        final boolean leapMonth = lunCalInfo.getLunLeapmonthAsBoolean();
        final AtomicInteger counter = new AtomicInteger();
        final int count = clientInstance().getSpcifyLunCalInfo(
                fromSolYear,
                toSolYear,
                lunMonth,
                lunDay,
                leapMonth,
                i -> {
                    assertThat(i.getLunMonthAsMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunDayAsDayOfMonth()).isNotNull().isEqualTo(lunDay);
                    counter.incrementAndGet();
                }
        );
        assertThat(count).isPositive().isEqualTo(counter.get());
    }
}
