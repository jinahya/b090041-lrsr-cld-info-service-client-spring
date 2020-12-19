package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ResponseTest {

    @Test
    void test_response01() throws Exception {
        try (InputStream resource = getClass().getResourceAsStream("response01.xml")) {
            assertThat(resource).isNotNull();
            final JAXBContext context = JAXBContext.newInstance(Response.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Response response = unmarshaller.unmarshal(new StreamSource(resource), Response.class).getValue();
            assertThat(response).isNotNull().satisfies(r -> {
                assertThat(r.getHeader()).isNotNull().satisfies(h -> {
                    assertThat(h.getResultCode()).isNotNull().isEqualTo(Response.Header.RESULT_CODE_SUCCESS);
                });
                assertThat(r.getBody()).isNotNull().satisfies(b -> {
                    assertThat(b.getItems()).isNotNull().hasSize(1).doesNotContainNull().allSatisfy(i -> {
                        assertThat(Validation.buildDefaultValidatorFactory().getValidator().validate(i)).isEmpty();
                        assertThat(i.getLunarDate()).isNotNull().isEqualTo(LocalDate.of(2020, 10, 30));
                        assertThat(i.getSolarDate()).isNotNull().isEqualTo(LocalDate.of(2020, 12, 14));
                    });
                });
            });
        }
    }

    @Test
    void test_response02() throws Exception {
        try (InputStream resource = getClass().getResourceAsStream("response02.xml")) {
            assertThat(resource).isNotNull();
            final JAXBContext context = JAXBContext.newInstance(Response.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Response response = unmarshaller.unmarshal(new StreamSource(resource), Response.class).getValue();
            assertThat(response).isNotNull().satisfies(r -> {
                assertThat(r.getHeader()).isNotNull().satisfies(h -> {
                    assertThat(h.getResultCode()).isNotNull().isEqualTo(Response.Header.RESULT_CODE_SUCCESS);
                });
                assertThat(r.getBody()).isNotNull().satisfies(b -> {
                    assertThat(b.getItems()).isNotNull().hasSize(1).doesNotContainNull().allSatisfy(i -> {
                        assertThat(Validation.buildDefaultValidatorFactory().getValidator().validate(i)).isEmpty();
                        assertThat(i.getLunWolgeon()).isNull();
                    });
                });
            });
        }
    }
}