# b090041-lrsrcldinfoservice-client-spring

[![Java CI with Maven](https://github.com/jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_datagokr-api-b090041-lrsrcldinfoservice-client-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=jinahya_datagokr-api-b090041-lrsrcldinfoservice-client-spring)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring)](https://search.maven.org/search?q=a:datagokr-api-b090041-lrsrcldinfoservice-client-spring)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring)

A client library for accessing http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService.

See [음양력 정보 (data.go.kr)](https://www.data.go.kr/data/15012679/openapi.do).

## Verify

Verify with your own service key assigned by the service provider.

```shell
$ mvn -Pfailsafe -DservcieKey=... clean verify
```

## Injection points

### Commaon

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@LrsrCldInfoServiceServiceKey`|`java.lang.String`|Provided by the service provider|
|                               |`javax.validation.Validator`||

### For `RestTemplate`

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@LrsrCldInfoServiceRestTemplate`|`o.s.web.client.RestTemplate`||
|`@LrsrCldInfoServiceRestTemplateRootUri`|`j.l.String`|Optional|

### For `WebClient`

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@LrsrCldInfoServiceWebClient`|`o.s.w.r.function.client.WebClient`||

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

Provide the service key assigned by the service provider. Note that the service provider may give you a URL-encoded
value. You should use a URL-decoded value.

```java
@AbstractLrsrCldInfoServiceClient.LrsrCldInfoServiceServiceKey
@Bean
String lrsrCldInfoServiceServiceKey(){
    // The service key assigned by data.go.kr
    // Might be already URL-encoded
    // Use a URL-decoded value    
    // return "...%3D%3D"; (X)
    // return "...==";     (O)
}
```

### Using `RestTeamplate`

Provide an instance of `RestTemplate`.

```java
@LrsrCldInfoServiceRestTemplate
@Bean
RestTemplate lrsrCldInfoServiceRestTemplate() {
    return new RestTemplateBuilder()
            ...
            .rootUri(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .build();
}
```

Get `@Autowired` with an instance of `LrsrCldInfoServiceClient` which is internally get autowired with
the `RestTemplate` instance.

```java
@Autowired
private LrsrCldInfoServiceClient client;
```

### Using `WebClient`

Provide an instance of `WebClient`.

```java
@LrsrCldInfoServiceWebClient
@Bean
WebClient lrsrCldInfoServiceWebClient() {
    return WebClient.builder()
            ...
            .baseUrl(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
            .build();
}
```

Get `@Autowired` with an instance of `LrsrCldInfoServiceReactiveClient` which is internally get autowired with the `WebClient` instance.

```java
@Autowired
private LrsrCldInfoServiceReactiveClient client;
```
