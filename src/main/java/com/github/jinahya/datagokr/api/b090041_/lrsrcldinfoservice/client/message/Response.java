package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * A class for binding {@code /:response} path.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@Slf4j
public class Response implements Serializable {

    private static final long serialVersionUID = -383243653796767676L;

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "header=" + header
               + ",body=" + body
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Response casted = (Response) obj;
        return Objects.equals(header, casted.header)
               && Objects.equals(body, casted.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty
    @Valid
    @NotNull
    @XmlElement(required = true)
    private Header header;

    @JsonProperty
    @Valid
    @NotNull
    @XmlElement(required = true)
    private Body body;
}
