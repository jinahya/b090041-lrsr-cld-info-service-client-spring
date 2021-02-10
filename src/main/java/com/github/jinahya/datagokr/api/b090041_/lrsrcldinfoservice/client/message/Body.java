package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A class for binding {@code /:response/:body} part.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Slf4j
public class Body implements Serializable {

    private static final long serialVersionUID = -4780774139453333151L;

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public Body() {
        super();
    }

    // -------------------------------------------------------------------------------- overridden from java.lang.Object

    /**
     * Returns the string representation of this object.
     *
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        return super.toString() + '{'
               + "items=" + items
               + ",numOfRows=" + numOfRows
               + ",pageNo=" + pageNo
               + ",totalCount=" + totalCount
               + '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Body that = (Body) obj;
        return numOfRows == that.numOfRows
               && pageNo == that.pageNo
               && totalCount == that.totalCount
               && Objects.equals(items, that.items);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(items, numOfRows, pageNo, totalCount);
    }

    // ------------------------------------------------------------------------------------------------------ pagination
    @JsonIgnore
    @XmlTransient
    public boolean isLastPage() {
        return numOfRows * pageNo >= totalCount;
    }

    // ------------------------------------------------------------------------------------------------- instance fields
    @NotNull
    @JsonProperty(required = true)
    @XmlElementWrapper
    @XmlElement(name = "item")
    private List<@Valid @NotNull Item> items;

    @PositiveOrZero
    @JsonProperty(required = true)
    @Positive
    @XmlElement(required = true)
    private int numOfRows;

    @PositiveOrZero
    @JsonProperty(required = true)
    @Positive
    @XmlElement(required = true)
    private int pageNo;

    @PositiveOrZero
    @JsonProperty(required = true)
    @PositiveOrZero
    @XmlElement(required = true)
    private int totalCount;
}
