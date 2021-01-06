# b090041-lrsrcldinfoservice-client-spring

[![Java CI with Maven](https://github.com/jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_datagokr-api-b090041-lrsrcldinfoservice-client-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=jinahya_datagokr-api-b090041-lrsrcldinfoservice-client-spring)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring)](https://search.maven.org/search?q=a:datagokr-api-b090041-lrsrcldinfoservice-client-spring)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring)

A client library for accessing http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService.

See [음양력 정보 (data.go.kr)](https://www.data.go.kr/data/15012679/openapi.do).

## Usages

Expand the component-scanning path.

```java
@SpringBootApplication(
        scanBasePackageClasses = {
                com.github.jinahya.datagokr.....client._NoOp.class,
                MyApplication.class
        }
)
class MyApplication {
}
```

Provide the service key assigned by the service provider. Note that the service provider may give you a URL-encoded value. You should use a URL-decoded value.

```java
@AbstractLrsrCldInfoServiceClient.LrsrCldInfoServiceServiceKey
@Bean
String lrsrCldInfoServiceServiceKey() {
    // The service key assigned by data.go.kr
    // ...%3D%3D (X)
    // ...==     (O)
}
```

### Using `RestTeamplate`

Provide an instance of `RestTemplate`.

```java
@LrsrCldInfoServiceClient.LrsrCldInfoServiceRestTemplate
@Bean
RestTemplate lrsrCldInfoServiceRestTemplate() {
    return new RestTemplateBuilder()
            ...
            .rootUri(AbstractLrsrCldInfoServiceClient.BASE_URL)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .build();
}
```

Get `@Autowired` with an instance of `LrsrCldInfoServiceClient` which is internally get autowired with the `RestTemplate` instance.

```java
@Autowired
private LrsrCldInfoServiceClient client;

void doSome() {
    for (final Item item : client.getLunCalInfo(LocalDate.now())) {
    }
}
```

### Using `WebClient`

Provide an instance of `WebClient`.

```java
@LrsrCldInfoServiceReactiveClient.LrsrCldInfoServiceWebClient
@Bean
WebClient lrsrCldInfoServiceWebClient(
    return WebClient.builder()
            ...
            .baseUrl(AbstractLrsrCldInfoServiceClient.BASE_URL)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .build();
}
```

Get `@Autowired` with an instance of `LrsrCldInfoServiceReactiveClient` which is internally get autowired with the `WebClient` instance.

```java
@Autowired
private LrsrCldInfoServiceReactiveClient client;

void doSome() {
    final LocalDate solarDate = LocalDate.now();
    client.getLunCalInfo(solarDate)
            .doOnNext(i -> {
            })
            .blockLast();
}
```
