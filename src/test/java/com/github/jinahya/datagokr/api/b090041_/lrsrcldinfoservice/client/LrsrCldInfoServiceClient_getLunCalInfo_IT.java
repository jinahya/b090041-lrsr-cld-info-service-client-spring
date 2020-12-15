package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    @EnabledIf("#{"
               + "systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null"
               + "}")
    @Test
    void test() {
        final LocalDate solarDate = LocalDate.now();
        final ResponseEntity<Response> responseEntity = clientInstance().getLunCalInfo(solarDate);
    }
}
