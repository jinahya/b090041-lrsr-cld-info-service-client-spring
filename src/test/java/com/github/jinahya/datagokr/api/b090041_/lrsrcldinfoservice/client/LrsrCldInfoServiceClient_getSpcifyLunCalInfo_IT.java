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
        final Response.Body.Item item = clientInstance().getLunCalInfo(LocalDate.now());
        final Year fromSolYear = item.getSolarYear().minusYears(1L);
        final Year toSolYear = item.getSolarYear().plusYears(1L);
        final Month lunMonth = item.getLunarMonth();
        final int lunDay = item.getLunarDayOfMonth();
        final boolean leapMonth = item.getLunarLeapMonth();
        final AtomicInteger counter = new AtomicInteger();
        final int count = clientInstance().getSpcifyLunCalInfo(
                fromSolYear,
                toSolYear,
                lunMonth,
                lunDay,
                leapMonth,
                i -> {
                    assertThat(i.getLunarMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(lunDay);
                    counter.incrementAndGet();
                }
        );
        assertThat(count).isPositive().isEqualTo(counter.get());
    }
}
