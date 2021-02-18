package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.reactive.function.client.WebClient;
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

    @Test
    void clientInstanceWebClient_NonNull_() {
        assertThat(clientInstance().webClient()).isNotNull();
    }

    @Test
    void getLunCalInfo__() {
        final Year year = Year.now();
        clientInstance().getLunCalInfo(year, getRuntime().availableProcessors(), Schedulers.parallel())
                .doOnNext(i -> {
                    assertThat(i.getSolarDate()).isNotNull().satisfies(d -> {
                        assertThat(Year.from(d)).isEqualTo(year);
                    });
                })
                .blockLast();
    }
}
