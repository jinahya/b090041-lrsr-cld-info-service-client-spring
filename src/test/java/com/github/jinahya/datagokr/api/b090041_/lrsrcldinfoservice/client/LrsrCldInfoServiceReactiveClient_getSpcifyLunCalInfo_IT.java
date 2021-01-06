package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.Month;
import java.time.Year;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void getSpcifyLunCalInfo() {
        final Year fromSolYear = Year.of(2018);
        final Year toSolYear = Year.of(2021);
        final Month lunMonth = Month.JANUARY;
        final int lunDay = 1;
        final boolean leapMonth = current().nextBoolean();
        clientInstance().getSpcifyLunCalInfo(fromSolYear, toSolYear, lunMonth, lunDay, leapMonth)
                .doOnNext(i -> {
                    log.debug("item: {}", i);
                    assertThat(i.getLunarMonth()).isNotNull().isEqualTo(lunMonth);
                    assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(lunDay);
                })
                .blockLast();
    }
}
