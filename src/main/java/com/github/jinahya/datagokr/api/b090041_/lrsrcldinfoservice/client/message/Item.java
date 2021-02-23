package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.Format02dIntegerAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.LeapBooleanAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.MmMonthAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.SolWeekDeserializer;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.SolWeekSerializer;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.SolWeekWeekAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.UuuuYearAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.JulianFields;
import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * A class for binding {@code /:response/:body/:item} part.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@Slf4j
public class Item implements Serializable {

    private static final long serialVersionUID = -4071620406720872635L;

    // ------------------------------------------------------------------------------------------------------- constants

    /**
     * A value for representing non-leaping year or month. The value is {@value}.
     *
     * @see #LEAP
     */
    public static final String NORMAL = "\ud3c9";

    /**
     * A value for representing a leaping year or month. The value is {@value}.
     */
    public static final String LEAP = "\uc724";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The minimum value of day-of-month in lunar calendar which is {@value}.
     *
     * @see #MAX_DAY_OF_MONTH_LUNAR
     */
    public static final int MIN_DAY_OF_MONTH_LUNAR = 1;

    /**
     * The maximum value of day-of-month in lunar calendar which is {@value}.
     *
     * @see #MIN_DAY_OF_MONTH_LUNAR
     */
    public static final int MAX_DAY_OF_MONTH_LUNAR = 30;

    /**
     * The minimum value of day-of-month in solar calendar which is {@value}.
     *
     * @see #MAX_DAY_OF_MONTH_SOLAR
     */
    public static final int MIN_DAY_OF_MONTH_SOLAR = 1;

    /**
     * The maximum value of day-of-month in solar calendar which is {@value}.
     *
     * @see #MIN_DAY_OF_MONTH_SOLAR
     */
    public static final int MAX_DAY_OF_MONTH_SOLAR = 31;

    // ----------------------------------------------------------------------------------------------------- comparators

    private static Comparator<Item> comparingLunarDate(final Comparator<Item> leapMonthComparator) {
        requireNonNull(leapMonthComparator, "leapMonthComparator is null");
        return comparing(Item::getLunYear)
                .thenComparing(Item::getLunMonth)
                .thenComparing(leapMonthComparator)
                .thenComparing(Item::getLunDay)
                ;
    }

    private static final Comparator<Item> LEAP_MONTH_LAST = comparing(Item::getLunLeapmonth);

    private static final Comparator<Item> LEAP_MONTH_FIRST = LEAP_MONTH_LAST.reversed();

    /**
     * The comparator compares items by lunar dates, leap months have precedences over non-leap months.
     */
    public static final Comparator<Item> COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST = comparingLunarDate(LEAP_MONTH_FIRST);

    /**
     * The comparator compares items by lunar dates, non-leap months have precedences over leap months.
     */
    public static final Comparator<Item> COMPARING_LUNAR_DATE_LEAP_MONTH_LAST = comparingLunarDate(LEAP_MONTH_LAST);

    // ------------------------------------------------------------------------------------------- comparators for solar

    /**
     * The comparator compares items by {@link #getSolarDate()}
     */
    public static final Comparator<Item> COMPARING_SOLAR_DATE = comparing(Item::getSolarDate);

    // ---------------------------------------------------------------------------------------------------- constructors

    /**
     * Creates a new instance.
     */
    public Item() {
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
               + "lunYear=" + lunYear
               + ",lunMonth=" + lunMonth
               + ",lunDay=" + lunDay
               + ",lunLeapmonth=" + lunLeapmonth
               + ",lunSecha=" + lunSecha
               + ",lunWolgeon=" + lunWolgeon
               + ",lunIljin=" + lunIljin
               + ",lunNday=" + lunNday
               + ",solYear=" + solYear
               + ",solMonth=" + solMonth
               + ",solDay=" + solDay
               + ",solLeapyear=" + solLeapyear
               + ",solWeek=" + solWeek
               + ",solJd=" + solJd
               + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(lunYear, item.lunYear)
               && lunMonth == item.lunMonth
               && Objects.equals(lunDay, item.lunDay)
               && Objects.equals(lunLeapmonth, item.lunLeapmonth)
               && Objects.equals(lunNday, item.lunNday)
               && Objects.equals(lunSecha, item.lunSecha)
               && Objects.equals(lunWolgeon, item.lunWolgeon)
               && Objects.equals(lunIljin, item.lunIljin)
               && Objects.equals(solYear, item.solYear)
               && solMonth == item.solMonth
               && Objects.equals(solDay, item.solDay)
               && Objects.equals(solLeapyear, item.solLeapyear)
               && solWeek == item.solWeek
               && Objects.equals(solJd, item.solJd)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                lunYear,
                lunMonth,
                lunDay,
                lunLeapmonth,
                lunNday,
                lunSecha,
                lunWolgeon,
                lunIljin,
                solYear,
                solMonth,
                solDay,
                solLeapyear,
                solWeek,
                solJd
        );
    }

    // ------------------------------------------------------------------------------------------------------------ JAXB
    void beforeUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        // has nothing to do.
    }

    @SuppressWarnings({"java:S1172"})
    void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        if (lunWolgeon != null && lunWolgeon.trim().isEmpty()) {
            lunWolgeon = null;
        }
    }

    // --------------------------------------------------------------------------------------------------------- lunYear

    // -------------------------------------------------------------------------------------------------------- lunMonth

    // ---------------------------------------------------------------------------------------------------------- lunDay

    // ---------------------------------------------------------------------------------------------------- lunLeapmomth

    // -------------------------------------------------------------------------------------------------------- lunSecha
    @JsonIgnore
    @XmlTransient
    public String getGanzhiYearKore() {
        return ofNullable(getLunSecha()).map(v -> v.substring(0, 2)).orElse(null);
    }

    @JsonIgnore
    @XmlTransient
    public String getGanzhiYearHans() {
        return ofNullable(getLunSecha()).map(v -> v.substring(3, 5)).orElse(null);
    }

    // ------------------------------------------------------------------------------------------------------ lunWolgeon
    @JsonIgnore
    @XmlTransient
    public String getGanzhiMonthKore() {
        return ofNullable(getLunWolgeon()).map(v -> v.substring(0, 2)).orElse(null);
    }

    @JsonIgnore
    @XmlTransient
    public String getGanzhiMonthHans() {
        return ofNullable(getLunWolgeon()).map(v -> v.substring(3, 5)).orElse(null);
    }

    // -------------------------------------------------------------------------------------------------------- lunIljin
    @JsonIgnore
    @XmlTransient
    public String getGanzhiDayKore() {
        return ofNullable(getLunIljin()).map(v -> v.substring(0, 2)).orElse(null);
    }

    @JsonIgnore
    @XmlTransient
    public String getGanzhiDayHans() {
        return ofNullable(getLunIljin()).map(v -> v.substring(3, 5)).orElse(null);
    }

    // --------------------------------------------------------------------------------------------------------- solYear

    // --------------------------------------------------------------------------------------------------------- lunNDay

    // -------------------------------------------------------------------------------------------------------- solMonth

    // ---------------------------------------------------------------------------------------------------------- solDay

    // ----------------------------------------------------------------------------------------------------- solLeapyear

    // --------------------------------------------------------------------------------------------------------- solWeek

    // ----------------------------------------------------------------------------------------------------------- solJd

    // ------------------------------------------------------------------------------------------------------- solarDate

    /**
     * Returns a local date of {@code solYear}, {@code solMonth}, and {@code solDay}.
     *
     * @return a local date of {@code solYear}, {@code solMonth}, and {@code solDay}.
     */
    @JsonIgnore
    @XmlTransient
    public LocalDate getSolarDate() {
        return LocalDate.of(requireNonNull(getSolYear(), "getSolYear() is null").getValue(),
                            requireNonNull(getSolMonth(), "getSolMonth() is null"),
                            requireNonNull(getSolDay(), "getSolDay() is null"));
    }

    /**
     * Replaces {@code solYear}, {@code solMonth}, {@code solDay}, {@code solLeapyear}, {@code solWeek}, and {@code
     * solJd} with specified solar local date.
     *
     * @param solarDate the solar local date.
     */
    public void setSolarDate(final LocalDate solarDate) {
        if (solarDate == null) {
            setSolYear(null);
            setSolMonth(null);
            setSolDay(null);
            setSolLeapyear(null);
            setSolWeek(null);
            setSolJd(null);
            return;
        }
        setSolYear(Year.from(solarDate));
        setSolMonth(solarDate.getMonth());
        setSolDay(solarDate.getDayOfMonth());
        setSolLeapyear(solarDate.isLeapYear());
        setSolWeek(solarDate.getDayOfWeek());
        setSolJd(solarDate.getLong(JulianFields.JULIAN_DAY));
    }

    // ----------------------------------------------------------------------------------------- lunar \ instance fields
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "yyyy")
    @JsonProperty(required = true)
    @NotNull
    @XmlJavaTypeAdapter(UuuuYearAdapter.class)
    @XmlSchemaType(name = "unsignedShort")
    @XmlElement(required = true)
    private Year lunYear;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "M")
    @JsonProperty(required = true)
    @NotNull
    @XmlJavaTypeAdapter(MmMonthAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Month lunMonth;

    @JsonProperty(required = true)
    @Max(MAX_DAY_OF_MONTH_LUNAR)
    @Min(MIN_DAY_OF_MONTH_LUNAR)
    @NotNull
    @XmlJavaTypeAdapter(Format02dIntegerAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Integer lunDay;

    @JsonProperty
    @NotNull
    @XmlJavaTypeAdapter(LeapBooleanAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Boolean lunLeapmonth;

    @JsonProperty(required = true)
    @Max(MAX_DAY_OF_MONTH_LUNAR)
    @Positive
    @XmlSchemaType(name = "unsignedByte")
    @XmlElement(required = true)
    private Integer lunNday;

    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String lunSecha;

    @JsonProperty
    @Nullable
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(nillable = true)
    private String lunWolgeon;

    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String lunIljin;

    // ----------------------------------------------------------------------------------------- solar \ instance fields
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "yyyy")
    @JsonProperty(required = true)
    @NotNull
    @XmlJavaTypeAdapter(UuuuYearAdapter.class)
    @XmlSchemaType(name = "unsignedShort")
    @XmlElement(required = true)
    private Year solYear;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "M")
    @JsonProperty(required = true)
    @NotNull
    @XmlJavaTypeAdapter(MmMonthAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Month solMonth;

    @JsonProperty(required = true)
    @Max(MAX_DAY_OF_MONTH_SOLAR)
    @Min(MIN_DAY_OF_MONTH_SOLAR)
    @NotNull
    @XmlJavaTypeAdapter(Format02dIntegerAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Integer solDay;

    @JsonProperty(required = true)
    @NotNull
    @XmlJavaTypeAdapter(LeapBooleanAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private Boolean solLeapyear;

    @JsonDeserialize(using = SolWeekDeserializer.class)
    @JsonSerialize(using = SolWeekSerializer.class)
    @NotNull
    @XmlJavaTypeAdapter(SolWeekWeekAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private DayOfWeek solWeek;

    @JsonProperty(required = true)
    @PositiveOrZero
    @NotNull
    @XmlSchemaType(name = "unsignedLong")
    @XmlElement(required = true)
    private Long solJd;
}
