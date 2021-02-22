package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Year;

import static java.lang.Runtime.getRuntime;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                LrsrCldInfoServiceReactiveClient_WithNoWebClientAutowired_IT._Configuration.class,
                LrsrCldInfoServiceReactiveClient.class
        }
)
@Slf4j
class LrsrCldInfoServiceReactiveClient_WithNoWebClientAutowired_IT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceReactiveClient> {

    @Import(AbstractLrsrCldInfoServiceClientIT._Configuration.class)
    @Configuration
    static class _Configuration {

        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        WebClient lrsrCldInfoServiceWebClient() {
            return null;
        }

        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }

    /**
     * Creates a new instance.
     */
    LrsrCldInfoServiceReactiveClient_WithNoWebClientAutowired_IT() {
        super(LrsrCldInfoServiceReactiveClient.class);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Test
    void clientInstanceWebClient_NonNull_() {
        assertThat(clientInstance().webClient()).isNotNull();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Invokes {@link LrsrCldInfoServiceReactiveClient#getLunCalInfo(Year, int, Scheduler)} method with current year and
     * asserts all items' {@code solYear} property equals to specified.
     */
    @Test
    void getLunCalInfo_SolarYearEquals_() {
        final Year year = Year.now();
        final int parallelism = getRuntime().availableProcessors();
        final Scheduler scheduler = Schedulers.parallel();
        clientInstance().getLunCalInfo(year, parallelism, scheduler)
                .doOnNext(i -> {
                    assertThat(i.getSolYear()).isNotNull().isEqualTo(year);
                })
                .blockLast();
    }

    /**
     * Invokes {@link LrsrCldInfoServiceReactiveClient#getSolCalInfo(Year, int, Scheduler)} method with current year and
     * asserts all items' {@code lunYear} property equals to specified.
     */
    @Test
    void getSolCalInfo_LunarYearEquals_() {
        final Year year = Year.now();
        final int parallelism = getRuntime().availableProcessors();
        final Scheduler scheduler = Schedulers.parallel();
        clientInstance().getSolCalInfo(year, parallelism, scheduler)
                .doOnNext(i -> {
                    assertThat(i.getLunYear()).isNotNull().isEqualTo(year);
                })
                .blockLast();
    }
}
