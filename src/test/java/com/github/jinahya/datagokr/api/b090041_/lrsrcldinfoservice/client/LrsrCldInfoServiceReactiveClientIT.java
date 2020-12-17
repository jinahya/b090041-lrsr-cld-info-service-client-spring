package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

@SpringBootTest(
        classes = {
                AbstractLrsrCldInfoServiceClientIT._Configuration.class,
                LrsrCldInfoServiceReactiveClientIT._Configuration.class,
                LrsrCldInfoServiceReactiveClient.class
        }
)
@Slf4j
abstract class LrsrCldInfoServiceReactiveClientIT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceReactiveClient> {

    @ComponentScan(
            basePackageClasses = {
                    _NoOp.class
            }
    )
    @Configuration
    static class _Configuration {

        static final int CONNECT_TIME_MILLIS = (int) (current().nextBoolean()
                                                      ? Duration.ofSeconds(3L).toMillis()
                                                      : TimeUnit.SECONDS.toMillis(3L));

        static final int READ_TIMEOUT_SECONDS = 3;

        static final int WRITE_TIMEOUT_SECONDS = 3;

        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        HttpClient httpClient() {
            return HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_MILLIS)
                    .doOnConnected(c -> {
                        c.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS));
                    });
        }

        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        ClientHttpConnector clientConnector(
                @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient final HttpClient httpClient) {
            return new ReactorClientHttpConnector(httpClient);
        }

        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        ExchangeStrategies exchangeStrategies() {
            final int byteCount = 256 * 1000; // 256K by default
            return ExchangeStrategies.builder()
                    .codecs(c -> {
                        c.defaultCodecs().maxInMemorySize(byteCount);
                    })
                    .build();
        }

        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        WebClient lrsrCldInfoServiceWebClient(
                @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient final ClientHttpConnector clientConnector,
                @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
                final ExchangeStrategies exchangeStrategies) {
            return WebClient.builder()
                    .clientConnector(clientConnector)
                    .exchangeStrategies(exchangeStrategies)
                    .filter((r, n) -> {
                        return n.exchange(r);
                    })
                    .baseUrl(AbstractLrsrCldInfoServiceClient.BASE_URL)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                    .build();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    LrsrCldInfoServiceReactiveClientIT() {
        super(LrsrCldInfoServiceReactiveClient.class);
    }
}
