package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.Nullable;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * A class for binding {@code /:response/:body/:item} path.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@Slf4j
public class Item extends RepresentationModel<Item> implements Serializable {

    private static final long serialVersionUID = -4071620406720872635L;

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A value for representing non-leaping year or month. The value is {@value}.
     *
     * @see #LEAP
     */
    public static final String NON_LEAP = "\ud3c9";

    /**
     * A value for representing a leaping year or month. The value is {@value}.
     */
    public static final String LEAP = "\uc724";

    private static final String PATTERN_REGEXP_NORMAL_OR_LEAP = '[' + NON_LEAP + LEAP + ']';

    // -----------------------------------------------------------------------------------------------------------------

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

    public static String formatDay(final int dayOfMonth) {
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new IllegalArgumentException("invalid dayOfMonth: " + dayOfMonth);
        }
        return format("%1$02d", dayOfMonth);
    }

    /**
     * The formatter for {@code solWeek}.
     */
    public static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("E", Locale.KOREAN);

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

    // -----------------------------------------------------------------------------------------------------------------
    public static final String REL_YEAR_LUNAR = "yearLunar";

    public static final String REL_MONTH_LUNAR = "monthLunar";

    public static final String REL_DATE_LUNAR = "dateLunar";

    public static final String REL_YEAR_SOLAR = "yearSolar";

    public static final String REL_MONTH_SOLAR = "monthSolar";

    public static final String REL_DATE_SOLAR = "dateSolar";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The comparator compares items by {@link #getLunLeapmonth()}.
     */
    private static final Comparator<Item> LEAP_MONTH_FIRST = comparing(Item::getLunarLeapMonth);

    /**
     * Returns a comparator {@link #getLunYear()}, {@link #getLunMonth()}, and {@link #getLunDay()}.
     *
     * @param leapMonthComparator a comparator for comparing {@link #getLunLeapmonth()}.
     * @see #LEAP_MONTH_FIRST
     */
    private static Comparator<Item> comparingInLunar(final Comparator<Item> leapMonthComparator) {
        requireNonNull(leapMonthComparator, "leapMonthComparator is null");
        return comparing(Item::getLunYear)
                .thenComparing(Item::getLunMonth)
                .thenComparing(leapMonthComparator)
                .thenComparing(Item::getLunDay)
                ;
    }

    /**
     * The comparator compares items by lunar dates, leap months have precedences over non-leap months.
     */
    public static final Comparator<Item> comparingInLunarLeapMonthFirst = comparingInLunar(LEAP_MONTH_FIRST);

    /**
     * The comparator compares items by lunar dates, non-leap months have precedences over leap months.
     */
    public static final Comparator<Item> comparingInLunarLeapMonthLast = comparingInLunar(LEAP_MONTH_FIRST.reversed());

    /**
     * The comparator compares items by {@link #getLunYear()}, {@link #getLunMonth()}, and {@link #getLunDay()},
     * regardless {@link #getLunLeapmonth()}.
     *
     * @see #COMPARING_IN_SOLAR
     * @see #comparingInLunarLeapMonthFirst
     * @see #comparingInLunarLeapMonthLast
     */
    public static final Comparator<Item> COMPARING_IN_LUNAR
            = comparing(Item::getLunYear)
            .thenComparing(Item::getLunMonth)
            .thenComparing(Item::getLunDay);

    /**
     * The comparator compares items by {@link #getSolYear()}, {@link #getSolMonth()}, and {@link #getSolDay()}.
     *
     * @see #COMPARING_IN_LUNAR
     */
    public static final Comparator<Item> COMPARING_IN_SOLAR
            = comparing(Item::getSolYear)
            .thenComparing(Item::getSolMonth)
            .thenComparing(Item::getSolDay);

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new instance.
     */
    public Item() {
        super();
    }

    // -----------------------------------------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------------------------------------
    void beforeUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        // has nothing to do.
    }

    @SuppressWarnings({"java:S1172"})
    void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
        if (lunWolgeon != null && lunWolgeon.trim().isEmpty()) {
            lunWolgeon = null;
        }
    }

    // ---------------------------------------------------------------------------- lunYear / lunarYearValue / lunarYear
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Integer getLunarYearValue() {
        return ofNullable(getLunYear())
                .map(Integer::parseInt)
                .orElse(null);
    }

    void setLunarYearValue(final Integer lunarYearValue) {
        setLunYear(ofNullable(lunarYearValue).map(v -> format("%1$d", v)).orElse(null));
    }

    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Year getLunarYear() {
        return ofNullable(getLunarYearValue()).map(Year::of).orElse(null);
    }

    public void setLunarYear(final Year lunarYear) {
        setLunarYearValue(ofNullable(lunarYear).map(Year::getValue).orElse(null));
    }

    // ------------------------------------------------------------------------- lunMonth / lunarMonthValue / lunarMonth
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Integer getLunarMonthValue() {
        return ofNullable(getLunMonth())
                .map(Integer::parseInt)
                .orElse(null);
    }

    public void setLunarMonthValue(final Integer lunarMonthValue) {
        setLunMonth(ofNullable(lunarMonthValue).map(v -> format("%1$02d", v)).orElse(null));
    }

    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Month getLunarMonth() {
        return ofNullable(getLunarMonthValue()).map(Month::of).orElse(null);
    }

    public void setLunarMonth(final Month lunarMonth) {
        setLunarMonthValue(ofNullable(lunarMonth).map(Month::getValue).orElse(null));
    }

    // ---------------------------------------------------------------------------------------- lunDay / lunarDayOfMonth
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Integer getLunarDayOfMonth() {
        return ofNullable(getLunDay())
                .map(Integer::parseInt)
                .orElse(null);
    }

    public void setLunarDayOfMonth(final Integer lunarDayOfMonth) {
        setLunDay(ofNullable(lunarDayOfMonth).map(v -> format("%02d", v)).orElse(null));
    }

    // ----------------------------------------------------------------------------------- lunLeapmonth / lunarLeapMonth
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Boolean getLunarLeapMonth() {
        return ofNullable(getLunLeapmonth()).map(LEAP::equals).orElse(null);
    }

    public void setLunarLeapMonth(final Boolean lunarLeapMonth) {
        setLunLeapmonth(ofNullable(lunarLeapMonth).map(v -> v.equals(Boolean.TRUE) ? LEAP : NON_LEAP).orElse(null));
    }

    // -------------------------------------------------------------------------------------------------------- lunSecha

    // ------------------------------------------------------------------------------------------------------ lunWolgeon

    // -------------------------------------------------------------------------------------------------------  lunIljin

    // ---------------------------------------------------------------------------------------- solYear / solarYearValue
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Integer getSolarYearValue() {
        return ofNullable(getSolYear())
                .map(Integer::parseInt)
                .orElse(null);
    }

    public void setSolarYearValue(final Integer solarYearValue) {
        setSolYear(ofNullable(solarYearValue).map(v -> format("%1$d", v)).orElse(null));
    }

    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Year getSolarYear() {
        return ofNullable(getSolarYearValue()).map(Year::of).orElse(null);
    }

    public void setSolarYear(final Year solarYear) {
        setSolarYearValue(ofNullable(solarYear).map(Year::getValue).orElse(null));
    }

    // ------------------------------------------------------------------------- solMonth / solarMonthValue / solarMonth
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Integer getSolarMonthValue() {
        return ofNullable(getSolMonth())
                .map(Integer::parseInt)
                .orElse(null);
    }

    public void setSolarMonthValue(final Integer solarMonthValue) {
        setSolMonth(ofNullable(solarMonthValue).map(v -> format("%1$02d", v)).orElse(null));
    }

    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Month getSolarMonth() {
        return ofNullable(getSolarMonthValue()).map(Month::of).orElse(null);
    }

    public void setSolarMonth(final Month solarMonth) {
        setSolarMonthValue(ofNullable(solarMonth).map(Month::getValue).orElse(null));
    }

    // ---------------------------------------------------------------------------------------- solDay / solarDayOfMonth
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public Integer getSolarDayOfMonth() {
        return ofNullable(getSolDay())
                .map(Integer::parseInt)
                .orElse(null);
    }

    public void setSolarDayOfMonth(final Integer solarDayOfMonth) {
        setSolDay(ofNullable(solarDayOfMonth).map(v -> format("%02d", v)).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------- solLeapyear

    // --------------------------------------------------------------------------------------------------------- solWeek

    // ----------------------------------------------------------------------------------------------------------- solJd

    // ------------------------------------------------------------------------------------------------------- solarDate
    @JsonbTransient
    @JsonIgnore
    @XmlTransient
    public LocalDate getSolarDate() {
        return LocalDate.of(requireNonNull(getSolarYearValue(), "getSolarYearValue() is null"),
                            requireNonNull(getSolarMonth(), "getSolarMonth() is null"),
                            requireNonNull(getSolarDayOfMonth(), "getSolarDayOfMonth() is null"));
    }

    public void setSolarDate(final LocalDate solarDate) {
        if (solarDate == null) {
            setSolarYearValue(null);
            setSolarMonthValue(null);
            setSolarDayOfMonth(null);
            return;
        }
        setSolarYearValue(solarDate.getYear());
        setSolarMonth(solarDate.getMonth());
        setSolarDayOfMonth(solarDate.getDayOfMonth());
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String lunYear;

    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String lunMonth;

    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String lunDay;

    @JsonbProperty
    @JsonProperty
    @Pattern(regexp = PATTERN_REGEXP_NORMAL_OR_LEAP)
    @NotNull
    @XmlElement
    private String lunLeapmonth;

    @JsonbProperty
    @JsonProperty
    @XmlElement
    private int lunNday;

    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String lunSecha;

    @JsonbProperty
    @JsonProperty
    @Nullable
    @XmlElement(required = false)
    private String lunWolgeon;

    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String lunIljin;

    // -----------------------------------------------------------------------------------------------------------------
    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String solYear;

    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String solMonth;

    @JsonbProperty
    @JsonProperty
    @NotBlank
    @XmlElement
    private String solDay;

    @JsonbProperty
    @JsonProperty
    @Pattern(regexp = PATTERN_REGEXP_NORMAL_OR_LEAP)
    @NotNull
    @XmlElement
    private String solLeapyear;

    @JsonbProperty
    @JsonProperty
    @Pattern(regexp = "[\uc6d4\ud654\uc218\ubaa9\uae08\ud1a0\uc77c]")
    @NotNull
    @XmlElement
    private String solWeek;

    @JsonbProperty
    @JsonProperty
    @PositiveOrZero
    @NotNull
    @XmlElement
    private Long solJd; // julian day, 율리우스일
}
