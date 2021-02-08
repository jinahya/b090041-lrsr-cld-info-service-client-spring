package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
 * A class for binding {@code /:response/:body/:item} path.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(access = AccessLevel.PACKAGE)
@Slf4j
public class Item implements Serializable {

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
        return format02d(dayOfMonth);
    }

    static String format02d(final Integer parsed) {
        return ofNullable(parsed).map(v -> format("%1$02d", v)).orElse(null);
    }

    static Integer parse02d(final String formatted) {
        return ofNullable(formatted).map(Integer::parseInt).orElse(null);
    }

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
    private static Comparator<Item> comparingLunarDate(final Comparator<Item> leapMonthComparator) {
        requireNonNull(leapMonthComparator, "leapMonthComparator is null");
        return comparing(Item::getLunarYear)
                .thenComparing(Item::getLunarMonth)
                .thenComparing(leapMonthComparator)
                .thenComparing(Item::getLunarDayOfMonth)
                ;
    }

    private static final Comparator<Item> LEAP_MONTH_LAST = comparing(Item::getLunarLeapMonth);

    private static final Comparator<Item> LEAP_MONTH_FIRST = LEAP_MONTH_LAST.reversed();

    /**
     * The comparator compares items by lunar dates, leap months have precedences over non-leap months.
     */
    public static final Comparator<Item> COMPARING_LUNAR_DATE_LEAP_MONTH_FIRST = comparingLunarDate(LEAP_MONTH_FIRST);

    /**
     * The comparator compares items by lunar dates, non-leap months have precedences over leap months.
     */
    public static final Comparator<Item> COMPARING_LUNAR_DATE_LEAP_MONTH_LAST = comparingLunarDate(LEAP_MONTH_LAST);

    /**
     * The comparator compares items by {@link #getSolarDate()}
     */
    public static final Comparator<Item> COMPARING_SOLAR_DATE = comparing(Item::getSolarDate);

    // -----------------------------------------------------------------------------------------------------------------
    private static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("E", Locale.KOREAN);

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

    // --------------------------------------------------------------------------------------------- lunYear / lunarYear
    @JsonIgnore
    @XmlTransient
    @NotNull
    public Year getLunarYear() {
        return ofNullable(getLunYear()).map(v -> Year.parse(v, YEAR_FORMATTER)).orElse(null);
    }

    public void setLunarYear(final Year lunarYear) {
        setLunYear(ofNullable(lunarYear).map(YEAR_FORMATTER::format).orElse(null));
    }

    // ------------------------------------------------------------------------------------------- lunMonth / lunarMonth
    @JsonIgnore
    @XmlTransient
    @NotNull
    public Month getLunarMonth() {
        return ofNullable(getLunMonth()).map(MONTH_FORMATTER::parse).map(Month::from).orElse(null);
    }

    public void setLunarMonth(final Month lunarMonth) {
        setLunMonth(ofNullable(lunarMonth).map(MONTH_FORMATTER::format).orElse(null));
    }

    // ---------------------------------------------------------------------------------------- lunDay / lunarDayOfMonth
    @JsonIgnore
    @XmlTransient
    @NotNull
    public Integer getLunarDayOfMonth() {
        return ofNullable(getLunDay())
                .map(Integer::parseInt)
                .orElse(null);
    }

    public void setLunarDayOfMonth(final Integer lunarDayOfMonth) {
        setLunDay(ofNullable(lunarDayOfMonth).map(Item::format02d).orElse(null));
    }

    // ----------------------------------------------------------------------------------- lunLeapmonth / lunarLeapMonth
    @JsonIgnore
    @XmlTransient
    @NotNull
    public Boolean getLunarLeapMonth() {
        return ofNullable(getLunLeapmonth()).map(LEAP::equals).orElse(null);
    }

    public void setLunarLeapMonth(final Boolean lunarLeapMonth) {
        setLunLeapmonth(ofNullable(lunarLeapMonth).map(v -> v.equals(Boolean.TRUE) ? LEAP : NON_LEAP).orElse(null));
    }

    // -------------------------------------------------------------------------------------------------------- lunSecha

    // ------------------------------------------------------------------------------------------------------ lunWolgeon

    // -------------------------------------------------------------------------------------------------------  lunIljin

    // --------------------------------------------------------------------------------------------- solYear / solarYear
    @JsonIgnore
    @XmlTransient
    @NotNull
    Year getSolarYear() {
        return ofNullable(getSolYear())
                .map(v -> Year.parse(v, YEAR_FORMATTER))
                .orElse(null);
    }

    public void setSolarYear(final Year solarYear) {
        setSolYear(ofNullable(solarYear).map(YEAR_FORMATTER::format).orElse(null));
    }

    // ------------------------------------------------------------------------------------------- solMonth / solarMonth
    @JsonIgnore
    @XmlTransient
    @NotNull
    Month getSolarMonth() {
        return ofNullable(getSolMonth()).map(MONTH_FORMATTER::parse).map(Month::from).orElse(null);
    }

    void setSolarMonth(final Month solarMonth) {
        setSolMonth(ofNullable(solarMonth).map(MONTH_FORMATTER::format).orElse(null));
    }

    // ---------------------------------------------------------------------------------------- solDay / solarDayOfMonth
    @JsonIgnore
    @XmlTransient
    @NotNull
    Integer getSolarDayOfMonth() {
        return ofNullable(getSolDay())
                .map(Integer::parseInt)
                .orElse(null);
    }

    void setSolarDayOfMonth(final Integer solarDayOfMonth) {
        setSolDay(ofNullable(solarDayOfMonth).map(Item::format02d).orElse(null));
    }

    // ----------------------------------------------------------------------------------------------------- solLeapyear

    // --------------------------------------------------------------------------------------------------------- solWeek

    // ----------------------------------------------------------------------------------------------------------- solJd

    // ------------------------------------------------------------------------------------------------------- solarDate
    @JsonIgnore
    @XmlTransient
    @NotNull
    public LocalDate getSolarDate() {
        return LocalDate.of(requireNonNull(getSolarYear(), "getSolarYear() is null").getValue(),
                            requireNonNull(getSolarMonth(), "getSolarMonth() is null"),
                            requireNonNull(getSolarDayOfMonth(), "getSolarDayOfMonth() is null"));
    }

    public void setSolarDate(final LocalDate solarDate) {
        if (solarDate == null) {
            setSolarYear(null);
            setSolarMonth(null);
            setSolarDayOfMonth(null);
            setSolLeapyear(null);
            setSolWeek(null);
            setSolJd(null);
            return;
        }
        setSolarYear(Year.from(solarDate));
        setSolarMonth(solarDate.getMonth());
        setSolarDayOfMonth(solarDate.getDayOfMonth());
        setSolLeapyear(solarDate.isLeapYear() ? LEAP : NON_LEAP);
        setSolWeek(WEEK_FORMATTER.format(solarDate));
        setSolJd(solarDate.getLong(JulianFields.JULIAN_DAY));
    }

    Item solarDate(final LocalDate solarDate) {
        setSolarDate(solarDate);
        return this;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String lunYear;

    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String lunMonth;

    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String lunDay;

    @JsonProperty
    @Pattern(regexp = PATTERN_REGEXP_NORMAL_OR_LEAP)
    @NotNull
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String lunLeapmonth;

    @JsonProperty(required = true)
    @Positive
    @XmlSchemaType(name = "unsignedByte")
    @XmlElement(required = true)
    private int lunNday;

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

    // -----------------------------------------------------------------------------------------------------------------
    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String solYear;

    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String solMonth;

    @JsonProperty(required = true)
    @NotBlank
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String solDay;

    @JsonProperty(required = true)
    @Pattern(regexp = PATTERN_REGEXP_NORMAL_OR_LEAP)
    @NotNull
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String solLeapyear;

    @JsonProperty(required = true)
    @Pattern(regexp = "[\uc6d4\ud654\uc218\ubaa9\uae08\ud1a0\uc77c]")
    @NotNull
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    @XmlElement(required = true)
    private String solWeek;

    @JsonProperty(required = true)
    @PositiveOrZero
    @NotNull
    @XmlSchemaType(name = "unsignedLong")
    @XmlElement(required = true)
    private Long solJd;
}
