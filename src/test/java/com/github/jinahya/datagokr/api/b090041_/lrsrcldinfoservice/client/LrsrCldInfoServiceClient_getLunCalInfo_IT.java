package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class LrsrCldInfoServiceClient_getLunCalInfo_IT extends LrsrCldInfoServiceClientIT {

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void test_getLunCalInfo_with_dayOfMonth() {
        final LocalDate now = LocalDate.now();
        final Year year = Year.of(now.getYear());
        final Month month = now.getMonth();
        final int dayOfMonth = now.getDayOfMonth();
        final Integer pageNo = null;
        final ResponseEntity<Response> entity = clientInstance().getLunCalInfo(year, month, dayOfMonth, pageNo);
        assertThat(entity).isNotNull().satisfies(e -> {
            assertThat(e.getStatusCode()).isNotNull().satisfies(status -> {
                assertThat(status.is2xxSuccessful()).isTrue();
            });
        });
        final Response response = entity.getBody();
        assertThat(response).isNotNull();
        final Response.Header header = response.getHeader();
        assertThat(header).isNotNull().satisfies(h -> {
            assertThat(h.isResultCodeSuccess()).isTrue();
        });
        final Response.Body body = response.getBody();
        assertThat(body).isNotNull().satisfies(b -> {
        });
        final List<Response.Body.Item> items = body.getItems();
        assertThat(items).isNotNull().doesNotContainNull();
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void test_getLunCalInfo_without_dayOfMonth() {
        final LocalDate now = LocalDate.now();
        final Year year = Year.of(now.getYear());
        final Month month = now.getMonth();
        final Integer dayOfMonth = null;
        for (int pageNo = 1; ; pageNo++) {
            final ResponseEntity<Response> entity = clientInstance().getLunCalInfo(year, month, dayOfMonth, pageNo);
            assertThat(entity).isNotNull().satisfies(e -> {
                assertThat(e.getStatusCode()).isNotNull().satisfies(status -> {
                    assertThat(status.is2xxSuccessful()).isTrue();
                });
            });
            final Response response = entity.getBody();
            assertThat(response).isNotNull();
            final Response.Header header = response.getHeader();
            assertThat(header).isNotNull().satisfies(h -> {
                assertThat(h.isResultCodeSuccess()).isTrue();
            });
            final Response.Body body = response.getBody();
            assertThat(body).isNotNull().satisfies(b -> {
            });
            final List<Response.Body.Item> items = body.getItems();
            assertThat(items).isNotNull().doesNotContainNull();
            if (items.isEmpty()) {
                break;
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void test_getLunCalInfo_dayOfMonth() {
        final LocalDate localDate = LocalDate.now();
        final Response.Body.Item item = clientInstance().getLunCalInfo(localDate);
        assertThat(item).isNotNull().satisfies(i -> {
            log.debug("item: {}", i);
            final String solYear = Response.Body.Item.YEAR_FORMATTER.format(localDate);
            final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(localDate);
            final String solDay = Response.Body.Item.DAY_OF_MONTH_FORMATTER.format(localDate);
            assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
            assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            assertThat(i.getSolDay()).isNotNull().isEqualTo(solDay);
        });
    }

    @EnabledIf("#{systemProperties['" + SYSTEM_PROPERTY_SERVICE_KEY + "'] != null}")
    @Test
    void test_getLunCalInfo_wholeMonth() {
        final LocalDate now = LocalDate.now();
        final Year year = Year.of(now.getYear());
        final Month month = now.getMonth();
        clientInstance().getLunCalInfo(year, month, item -> {
            final String solYear = Response.Body.Item.YEAR_FORMATTER.format(year);
            final String solMonth = Response.Body.Item.MONTH_FORMATTER.format(month);
            assertThat(item).isNotNull().satisfies(i -> {
                log.debug("item: {}", item);
                assertThat(i.getSolYear()).isNotNull().isEqualTo(solYear);
                assertThat(i.getSolMonth()).isNotNull().isEqualTo(solMonth);
            });
        });
    }
}
