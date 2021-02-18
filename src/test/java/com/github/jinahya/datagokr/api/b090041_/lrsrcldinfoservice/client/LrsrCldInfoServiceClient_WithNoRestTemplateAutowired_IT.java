package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                LrsrCldInfoServiceClient_WithNoRestTemplateAutowired_IT._Configuration.class,
                LrsrCldInfoServiceClient.class
        }
)
@Slf4j
class LrsrCldInfoServiceClient_WithNoRestTemplateAutowired_IT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceClient> {

    @Import(AbstractLrsrCldInfoServiceClientIT._Configuration.class)
    @Configuration
    static class _Configuration {

        @LrsrCldInfoServiceClient.LrsrCldInfoServiceRestTemplate
        @Bean
        RestTemplate lrsrCldInfoServiceRestTemplate() {
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
    LrsrCldInfoServiceClient_WithNoRestTemplateAutowired_IT() {
        super(LrsrCldInfoServiceClient.class);
    }

    @Test
    void clientInstanceRestTemplate_NonNull_() {
        assertThat(clientInstance().restTemplate()).isNotNull();
    }

    @Test
    void clientInstanceRestTemplateRootUri_NonBlank_() {
        assertThat(clientInstance().restTemplateRootUri()).isNotBlank();
    }

    @Test
    void getLunCalInfo__Year() {
        final Year year = Year.now();
        final List<Item> items = clientInstance().getLunCalInfo(year, commonPool(), new ArrayList<>());
        assertThat(items)
                .isNotNull()
                .isNotEmpty()
                .doesNotContainNull()
                .allSatisfy(i -> {
                    assertThat(i.getSolarDate()).isNotNull().satisfies(d -> {
                        assertThat(Year.from(d)).isEqualTo(year);
                    });
                });
    }
}
