package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceReactiveClient_getSpcifyLunCalInfo_IT extends LrsrCldInfoServiceReactiveClientIT {

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @DisplayName("getSpcifyLunCalInfo(fromSolarYear, toSolarYear, lunarMonth, lunarDayOfMonth, lunarLeapMonth)")
    @Test
    void getSpcifyLunCalInfo() {
        final Response.Body.Item item = clientInstance().getLunCalInfo(LocalDate.now().withDayOfMonth(1)).blockLast();
        assertThat(item).isNotNull();
        clientInstance().getSpcifyLunCalInfo(item.getSolarYear().minusYears(1L), item.getSolarYear().plusYears(1L),
                                             item.getLunarMonth(), item.getLunarDayOfMonth(), item.getLunarLeapMonth())
                .doOnNext(i -> {
                    assertThat(i.getLunarMonth()).isNotNull().isEqualTo(item.getLunarMonth());
                    assertThat(i.getLunarDayOfMonth()).isNotNull().isEqualTo(item.getLunarDayOfMonth());
                })
                .blockLast();
    }
}
