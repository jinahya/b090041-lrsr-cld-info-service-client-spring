# b090041-lrsrcldinfoservice-client-spring

[![Java CI with Maven](https://github.com/jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jinahya_datagokr-api-b090041-lrsrcldinfoservice-client-spring&metric=alert_status)](https://sonarcloud.io/dashboard?id=jinahya_datagokr-api-b090041-lrsrcldinfoservice-client-spring)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring)](https://search.maven.org/search?q=a:datagokr-api-b090041-lrsrcldinfoservice-client-spring)
[![javadoc](https://javadoc.io/badge2/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring/javadoc.svg)](https://javadoc.io/doc/com.github.jinahya/datagokr-api-b090041-lrsrcldinfoservice-client-spring)

A client library for accessing http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService.

See [음양력정보(data.go.kr)](https://www.data.go.kr/data/15012679/openapi.do) and/or [월별음약력(천문연구원)](https://astro.kasi.re.kr/life/pageView/5).

## Verify

Verify with your own service key assigned by the service provider.

```shell
$ mvn -Pfailsafe -DservcieKey=... clean verify
```

You may put your service key on `src/test/resources/failsafe.system.properties`, which is git-ignored, like this,
```
serviceKey=...
```
and verify like this.
```shell
$ mvn -Pfailsafe clean verify
```

## Injection points

### Common

|Qualifier|Type|Notes|
|---------|----|-----------|
|`@LrsrCldInfoServiceServiceKey`|`java.lang.String`|Provided by the service provider|

### For `LrsrCldInfoServiceClient` with `RestTemplate`

|Qualifier|Type|Notes|
|---------|----|-----|
|`@LrsrCldInfoServiceRestTemplate`|[`RestTemplate`][RestTemplate]||
|`@LrsrCldInfoServiceRestTemplateRootUri`|`String`|Optional|

### For `LrsrCldInfoServiceReactiveClient` with `WebClient`

|Qualifier|Type|Notes|
|---------|----|-----|
|`@LrsrCldInfoServiceWebClient`|[`WebClient`][WebClient]||

## Usages

Expand the component-scanning path.

```java

@SpringBootApplication(
        scanBasePackageClasses = {
                com.github.jinahya.datagokr.....client.NoOp.class,
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
public String lrsrCldInfoServiceServiceKey(){
    // The service key assigned by data.go.kr
    // Might be already URL-encoded
    // Use a URL-decoded value    
    // return "...%3D%3D"; (X)
    // return "...==";     (O)
}
```

### Using `LrsrCldInfoServiceClient` with `RestTemplate`

Provide an instance of `RestTemplate`.

```java
@LrsrCldInfoServiceRestTemplate
@Bean
public RestTemplate lrsrCldInfoServiceRestTemplate() {
    return new RestTemplateBuilder()
            ...
            .rootUri(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
            .build();
}
```

Get `@Autowired` with an instance of `LrsrCldInfoServiceClient` which is internally got autowired with the `RestTemplate` instance.

```java
@Autowired
private LrsrCldInfoServiceClient client;
```

### Using `LrsrCldInfoServiceReactiveClient` with `WebClient`

Provide an instance of `WebClient`.

```java
@LrsrCldInfoServiceWebClient
@Bean
public WebClient lrsrCldInfoServiceWebClient() {
    return WebClient.builder()
            ...
            .baseUrl(AbstractLrsrCldInfoServiceClient.BASE_URL_PRODUCTION)
            .build();
}
```

Get `@Autowired` with an instance of `LrsrCldInfoServiceReactiveClient` which is internally got autowired with the `WebClient` instance.

```java
@Autowired
private LrsrCldInfoServiceReactiveClient client;
```


[RestTemplate]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
[WebClient]: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/client/WebClient.html