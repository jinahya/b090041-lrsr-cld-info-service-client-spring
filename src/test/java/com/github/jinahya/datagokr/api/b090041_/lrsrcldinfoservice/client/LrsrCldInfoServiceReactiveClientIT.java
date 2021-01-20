package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;

@SpringBootTest(
        classes = {
                LrsrCldInfoServiceReactiveClientIT._Configuration.class,
                LrsrCldInfoServiceReactiveClient.class
        }
)
@Slf4j
// https://stackoverflow.com/q/48992992/330457
// https://stackoverflow.com/q/48096573/330457
abstract class LrsrCldInfoServiceReactiveClientIT
        extends AbstractLrsrCldInfoServiceClientIT<LrsrCldInfoServiceReactiveClient> {

    @Import(AbstractLrsrCldInfoServiceClientIT._Configuration.class)
    @Configuration
    static class _Configuration {

        static final int CONNECT_TIME_MILLIS = (int) Duration.ofSeconds(10L).toMillis();

        static final int READ_TIMEOUT_SECONDS = 30;

        static final int WRITE_TIMEOUT_SECONDS = 10;

        @Deprecated
        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        TcpClient tcpClient() {
            return TcpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_MILLIS)
                    .doOnConnected(c -> c.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS)));
        }

        @LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
        @Bean
        HttpClient httpClient() {
//            return HttpClient.create() // Spring Boot 2.4.X
//                    //.responseTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS)) // since reactor-netty:0.9.11
//                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_MILLIS)
//                    .doOnConnected(c -> {
//                        c.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS))
//                                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS));
//                    });
            return HttpClient.from(tcpClient());
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
                    .baseUrl(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                    .build();
        }

        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
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
