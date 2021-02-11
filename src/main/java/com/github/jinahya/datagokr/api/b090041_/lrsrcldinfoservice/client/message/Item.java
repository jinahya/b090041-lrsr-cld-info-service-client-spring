package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.Format02dIntegerAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.LeapBooleanAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.MmMonthAdapter;
import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter.SingleKoreanDayOfWeekAdapter;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.JulianFields;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * A class for binding {@code /:response/:body/:item} part.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Setter//(AccessLevel.PROTECTED)
@Getter//(AccessLevel.PROTECTED)
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

    private static final String PATTERN_REGEXP_NORMAL_OR_LEAP = '[' + NORMAL + LEAP + ']';

    static Boolean leapFlag(final String text) {
        return ofNullable(text).map(LEAP::equals).orElse(null);
    }

    static String leapText(final Boolean flag) {
        return ofNullable(flag).map(v -> Boolean.TRUE.equals(v) ? LEAP : NORMAL).orElse(null);
    }

    // ------------------------------------------------------------------------------------------------------ formatters

    /**
     * The formatter for {@code solYear} and {@code lunYear}.
     */
    public static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("uuuu");

    /**
     * The formatter for {@code solMonth} and {@code lunMonth}.
     */
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");

    /**
     * The formatter for {@code solDay} and {@code lunDay}.
     */
    public static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");

    /**
     * Formats specified day-of-month value as {@code %02d}.
     *
     * @param dayOfMonth the value to format.
     * @return a formatted string.
     */
    public static String formatDay(final int dayOfMonth) {
        if (dayOfMonth < MIN_DAY_OF_MONTH_SOLAR || dayOfMonth > MAX_DAY_OF_MONTH_SOLAR) {
            throw new IllegalArgumentException("invalid dayOfMonth: " + dayOfMonth);
        }
        return format02d(dayOfMonth);
    }

    /**
     * Formats specified as {@code %02d}.
     *
     * @param parsed the value to format.
     * @return a formatted string.
     */
    static String format02d(final Integer parsed) {
        return ofNullable(parsed).map(v -> format("%1$02d", v)).orElse(null);
    }

    // TODO: Remove, unused.
    @Deprecated
    static Integer parse02d(final String formatted) {
        return ofNullable(formatted).map(Integer::parseInt).orElse(null);
    }

    /**
     * A formatter for formatting {@link java.time.temporal.ChronoField#DAY_OF_WEEK} with {@link Locale#KOREAN KOREAN}
     * locale.
     */
    static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("E", Locale.KOREAN);

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

    // ----------------------------------------------------------------------------------------------------- comparators

    /**
     * The maximum value of day-of-month in solar calendar which is {@value}.
     *
     * @see #MIN_DAY_OF_MONTH_SOLAR
     */
    public static final int MAX_DAY_OF_MONTH_SOLAR = 31;

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
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final Item that = (Item) obj;
        return lunNday == that.lunNday
               && Objects.equals(lunYear, that.lunYear)
               && Objects.equals(lunMonth, that.lunMonth)
               && Objects.equals(lunDay, that.lunDay)
               && Objects.equals(lunLeapmonth, that.lunLeapmonth)
               && Objects.equals(lunSecha, that.lunSecha)
               && Objects.equals(lunWolgeon, that.lunWolgeon)
               && Objects.equals(lunIljin, that.lunIljin)
               && Objects.equals(solYear, that.solYear)
               && Objects.equals(solMonth, that.solMonth)
               && Objects.equals(solDay, that.solDay)
               && Objects.equals(solLeapyear, that.solLeapyear)
               && Objects.equals(solWeek, that.solWeek)
               && Objects.equals(solJd, that.solJd);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
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

    // --------------------------------------------------------------------------------------------------------- solYear

    // -------------------------------------------------------------------------------------------------------- solMonth

    // ---------------------------------------------------------------------------------------------------------- solDay

    // ----------------------------------------------------------------------------------------------------- solLeapyear

    // --------------------------------------------------------------------------------------------------------- solWeek
    @JsonProperty(value = "solWeek", required = true)
    @XmlTransient
    public Integer getSolWeekAsInteger() {
        return ofNullable(getSolWeek()).map(DayOfWeek::getValue).orElse(null);
    }

    public void setSolWeekAsInteger(final Integer solWeekAsInteger) {
        setSolWeek(ofNullable(solWeekAsInteger).map(DayOfWeek::of).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------------- solJd
    @JsonIgnore
    @XmlTransient
    public Long getSolarJulianDay() {
        return getSolJd();
    }

    void setSolarJulianDay(final Long solarJulianDay) {
        setSolJd(solarJulianDay);
    }

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
            setSolarJulianDay(null);
            return;
        }
        setSolYear(Year.from(solarDate));
        setSolMonth(solarDate.getMonth());
        setSolDay(solarDate.getDayOfMonth());
        setSolLeapyear(solarDate.isLeapYear());
        setSolWeek(solarDate.getDayOfWeek());
        setSolarJulianDay(solarDate.getLong(JulianFields.JULIAN_DAY));
    }

    Item solarDate(final LocalDate solarDate) {
        setSolarDate(solarDate);
        return this;
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

    //@JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "E")
    //@JsonProperty(required = true)
    @JsonIgnore
    @NotNull
    @XmlJavaTypeAdapter(SingleKoreanDayOfWeekAdapter.class)
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
