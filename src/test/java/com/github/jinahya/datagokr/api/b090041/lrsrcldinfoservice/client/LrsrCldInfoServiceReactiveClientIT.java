package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client.LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient;
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
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.ThreadLocalRandom.current;

@SpringBootTest(
        classes = {
                AbstractLrsrCldInfoServiceClientIT._Configuration.class,
                LrsrCldInfoServiceReactiveClientIT._Configuration.class
        }
)
@Slf4j
abstract class LrsrCldInfoServiceReactiveClientIT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceReactiveClient> {

    @ComponentScan(basePackageClasses = {_NoOp.class})
    @Configuration
    static class _Configuration {

        static int CONNECT_TIME_MILLIS = (int) (current().nextBoolean()
                                                ? Duration.ofSeconds(3L).toMillis()
                                                : TimeUnit.SECONDS.toMillis(3L));

        static int READ_TIMEOUT_SECONDS = 3;

        static int WRITE_TIMEOUT_SECONDS = 3;

        @LrsrCldInfoServiceWebClient
        @Bean
        TcpClient tcpClient() {
            return TcpClient
                    .create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_MILLIS)
                    .doOnConnected(c -> {
                        c.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS));
                    });
        }

        @LrsrCldInfoServiceWebClient
        @Bean
        ClientHttpConnector clientConnector(@LrsrCldInfoServiceWebClient final TcpClient tcpClient) {
            return new ReactorClientHttpConnector(HttpClient.from(tcpClient));
        }

        @LrsrCldInfoServiceWebClient
        @Bean
        ExchangeStrategies exchangeStrategies() {
            final int byteCount = 256 * 1000; // 256K by default
            return ExchangeStrategies.builder()
                    .codecs(c -> {
                        c.defaultCodecs().maxInMemorySize(byteCount);
                    })
                    .build();
        }

        @LrsrCldInfoServiceWebClient
        @Bean
        WebClient lrsrCldInfoServiceWebClient(
                @LrsrCldInfoServiceWebClient final ClientHttpConnector clientConnector,
                @LrsrCldInfoServiceWebClient final ExchangeStrategies exchangeStrategies) {
            return WebClient.builder()
                    .clientConnector(clientConnector)
                    .exchangeStrategies(exchangeStrategies)
                    .filter((r, n) -> {
                        log.debug("filtering with {} and {}", r, n);
                        return n.exchange(r);
                    })
                    .baseUrl(System.getProperty(SYSTEM_PROPERTY_URL))
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
