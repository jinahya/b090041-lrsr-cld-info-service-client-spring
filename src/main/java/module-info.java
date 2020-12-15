module com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client {
    requires java.annotation;
    requires java.validation;
    requires java.xml.bind;
    requires lombok;
    requires org.slf4j;
    requires reactor.core;
    requires spring.beans;
    requires spring.context;
    requires spring.web;
    requires spring.webflux;
    requires spring.core;
    opens com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client to spring.core;
    opens com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message to java.xml.bind;
    exports com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;
    exports com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;
}