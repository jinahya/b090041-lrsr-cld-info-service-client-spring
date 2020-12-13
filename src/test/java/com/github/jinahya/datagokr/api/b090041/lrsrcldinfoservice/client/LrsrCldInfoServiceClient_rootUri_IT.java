package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                AbstractLrsrCldInfoServiceClientIT._Configuration.class,
                LrsrCldInfoServiceClient_rootUri_IT._Configuration.class
        }
)
@Slf4j
class LrsrCldInfoServiceClient_rootUri_IT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceClient> {

    // -----------------------------------------------------------------------------------------------------------------
    @ComponentScan( // i honestly don't know why this annotation is required here
                    )
    @Configuration
    static class _Configuration {

        @LrsrCldInfoServiceClient.LrsrCldInfoServiceRestTemplateRootUri
        @Bean
        String lrsrCldInfoServiceRestTemplateRootUri() {
            return System.getProperty(SYSTEM_PROPERTY_URL);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    LrsrCldInfoServiceClient_rootUri_IT() {
        super(LrsrCldInfoServiceClient.class);
    }

    @Test
    void testRootUri_() {
        assertThat(clientInstance().restTemplateRootUri).isNotNull().isEqualTo(rootUri);
        assertThat(clientInstance().uriBuilderFromRootUriPathApi()).isNotNull().satisfies(b -> {
            assertThat(b.build().toUriString()).isNotNull().isEqualTo(rootUri + "/api");
        });
    }

    @Test
    void test() {
        final String url = "http://base.url";
        final RestTemplate template = new RestTemplate();
        assertThat(template.getUriTemplateHandler()).isNotNull();
        final DefaultUriTemplateHandler handler = new DefaultUriTemplateHandler();
        handler.setBaseUrl(url);
        template.setUriTemplateHandler(handler);
        final URI expanded = template.getUriTemplateHandler().expand("/"); // this is what the client class does
        assertThat(expanded).isNotNull().isEqualTo(URI.create(url + "/"));
    }

    @LrsrCldInfoServiceClient.LrsrCldInfoServiceRestTemplateRootUri
    @Autowired
    private String rootUri;
}
