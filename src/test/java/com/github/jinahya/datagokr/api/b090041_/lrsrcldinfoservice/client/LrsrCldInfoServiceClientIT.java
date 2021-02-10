package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootTest(
        classes = {
                LrsrCldInfoServiceClientIT._Configuration.class,
                LrsrCldInfoServiceClient.class
        }
)
@Slf4j
class LrsrCldInfoServiceClientIT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceClient> {

    @Import(AbstractLrsrCldInfoServiceClientIT._Configuration.class)
    @Configuration
    static class _Configuration {

        @LrsrCldInfoServiceClient.LrsrCldInfoServiceRestTemplate
        @Bean
        RestTemplate lrsrCldInfoServiceRestTemplate() {
            return new RestTemplateBuilder()
                    .additionalCustomizers(
                            t -> {
                                log.debug("1 customizing {}", t);
                            },
                            t -> {
                                log.debug("2 customizing {}", t);
                                t.setRequestFactory(new BufferingClientHttpRequestFactory(t.getRequestFactory()));
                            }
                    )
                    .additionalInterceptors(
                            (r, b, e) -> {
//                                log.debug("1 executing with ({}, {})", r, b);
                                return e.execute(r, b);
                            },
                            (r, b, e) -> {
//                                log.debug("2 executing with ({}, {})", r, b);
                                return e.execute(r, b);
                            }
                    )
                    .setConnectTimeout(Duration.ofSeconds(10L))
                    .setReadTimeout(Duration.ofSeconds(60L))
                    .rootUri(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
//                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE) // n/a with 2.1.18.RELEASE
                    .build();
        }

        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }

    /**
     * Creates a new instance.
     */
    LrsrCldInfoServiceClientIT() {
        super(LrsrCldInfoServiceClient.class);
    }
}
