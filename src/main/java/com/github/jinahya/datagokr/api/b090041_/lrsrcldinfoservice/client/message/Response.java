package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
@Slf4j
public class Response {

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @Slf4j
    public static class Header {

        public static final String RESULT_CODE_SUCCESS = "00";

        public boolean isResultCodeSuccess() {
            return RESULT_CODE_SUCCESS.equals(resultCode);
        }

        @NotBlank
        private String resultCode;

        @NotBlank
        private String resultMsg;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Slf4j
    public static class Body {

        @XmlAccessorType(XmlAccessType.FIELD)
        @Setter
        @Getter
        @Slf4j
        public static class Item implements Serializable {

            private static final long serialVersionUID = -4071620406720872635L;

            // ---------------------------------------------------------------------------------------------------------
            public static final String NORMAL = "\ud3c9";

            public static final String LEAP = "\uc724";

            private static final String PATTERN_REGEXP_NORMAL_OR_LEAP = '[' + NORMAL + LEAP + ']';

            // ---------------------------------------------------------------------------------------------------------

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
             * The formatter for {@code solWeek}.
             */
            public static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("E", Locale.KOREAN);

            // ---------------------------------------------------------------------------------------------------------
            public static final Comparator<Item> COMPARING_IN_LUNAR = Comparator.comparing(Item::getLunarYear)
                    .thenComparing(Item::getLunMonth)
                    .thenComparing(Item::getLunDay);

            public static final Comparator<Item> COMPARING_IN_SOLAR = Comparator.comparing(Item::getSolarDate);

            // ---------------------------------------------------------------------------------------------------------

            /**
             * Creates a new instance.
             */
            public Item() {
                super();
            }

            // ---------------------------------------------------------------------------------------------------------
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

            // ---------------------------------------------------------------------------------------------------------
            void beforeUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
                // has nothing to do.
            }

            @SuppressWarnings({"java:S1172"})
            void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
                if (lunWolgeon != null && lunWolgeon.trim().isEmpty()) {
                    lunWolgeon = null;
                }
            }

            // ------------------------------------------------------------------------------------- lunYear / lunarYear
            public Year getLunarYear() {
                return ofNullable(getLunYear()).map(v -> Year.parse(v, YEAR_FORMATTER)).orElse(null);
            }

            // ----------------------------------------------------------------------------------- lunMonth / lunarMonth
            public Month getLunarMonth() {
                return ofNullable(getLunMonth()).map(Integer::parseInt).map(Month::of).orElse(null);
            }

            // -------------------------------------------------------------------------------- lunDay / lunarDayOfMonth
            public Integer getLunarDayOfMonth() {
                return ofNullable(getLunDay()).map(Integer::parseInt).orElse(null);
            }

            // --------------------------------------------------------------------------- lunLeapmonth / lunarLeapMonth
            public Boolean getLunarLeapMonth() {
                return LEAP.equals(getLunLeapmonth());
            }

            // ------------------------------------------------------------------------------------------------ lunSecha

            // ---------------------------------------------------------------------------------------------- lunWolgeon

            // -----------------------------------------------------------------------------------------------  lunIljin

            // ------------------------------------------------------------------------------------------------- lunYear

            // ----------------------------------------------------------------------------------------------- solarYear
            public Year getSolarYear() {
                return ofNullable(getSolYear()).map(v -> Year.parse(v, YEAR_FORMATTER)).orElse(null);
            }

            void setSolarYear(final Year solarYear) {
                setSolYear(ofNullable(solarYear).map(YEAR_FORMATTER::format).orElse(null));
            }

            // ------------------------------------------------------------------------------------------------ solMonth

            // ---------------------------------------------------------------------------------------------- solarMonth
            public Month getSolarMonth() {
                return ofNullable(getSolMonth()).map(Integer::parseInt).map(Month::of).orElse(null);
            }

            void setSolarMonth(final Month solarMonth) {
                setSolMonth(ofNullable(solarMonth).map(MONTH_FORMATTER::format).orElse(null));
            }

            // -------------------------------------------------------------------------------------------------- solDay

            // ----------------------------------------------------------------------------------------- solarDayOfMonth
            public Integer getSolarDayOfMonth() {
                return ofNullable(getSolDay()).map(Integer::parseInt).orElse(null);
            }

            void setSolarDayOfMonth(final Integer solarDayOfMonth) {
                setSolDay(ofNullable(solarDayOfMonth).map(v -> format("%1$02d", v)).orElse(null));
            }

            // ----------------------------------------------------------------------------------------------- solarDate
            public LocalDate getSolarDate() {
                return LocalDate.of(getSolarYear().getValue(), getSolarMonth(), getSolarDayOfMonth());
            }

            public void setSolarDate(final LocalDate solarDate) {
                setSolarYear(ofNullable(solarDate).map(Year::from).orElse(null));
                setSolarMonth(ofNullable(solarDate).map(Month::from).orElse(null));
                setSolarDayOfMonth(ofNullable(solarDate).map(LocalDate::getDayOfMonth).orElse(null));
                setSolarLeapYear(ofNullable(solarDate).map(LocalDate::isLeapYear).orElse(null));
                setSolarDayOfWeek(ofNullable(solarDate).map(DayOfWeek::from).orElse(null));
                setSolarJulianDay(ofNullable(solarDate).map(v -> v.getLong(JulianFields.JULIAN_DAY)).orElse(null));
            }

            // --------------------------------------------------------------------------------------------- solLeapyear
            @AssertTrue
            private boolean isSolLeapyearValid() {
                return getSolarDate().isLeapYear() == getSolarLeapYear();
            }

            // ------------------------------------------------------------------------------------------- solarLeapYear
            public Boolean getSolarLeapYear() {
                return ofNullable(getSolLeapyear()).map(v -> v.equals(LEAP)).orElse(null);
            }

            public void setSolarLeapYear(final Boolean solarLeapYear) {
                setSolLeapyear(ofNullable(solarLeapYear).map(v -> Boolean.TRUE.equals(v) ? LEAP : NORMAL).orElse(null));
            }

            // ------------------------------------------------------------------------------------------------- solWeek

            // ------------------------------------------------------------------------------------------ solarDayOfWeek
            @NotNull
            public DayOfWeek getSolarDayOfWeek() {
                return ofNullable(getSolWeek()).map(WEEK_FORMATTER::parse).map(DayOfWeek::from).orElse(null);
            }

            void setSolarDayOfWeek(final DayOfWeek solarDayOfWeek) {
                setSolWeek(ofNullable(solarDayOfWeek).map(WEEK_FORMATTER::format).orElse(null));
            }

            // ---------------------------------------------------------------------------------- solJd / solarJulianDay
            @AssertTrue
            private boolean isSolJdValid() {
                return solJd == getSolarDate().getLong(JulianFields.JULIAN_DAY);
            }

            public Long getSolarJulianDay() {
                return getSolJd();
            }

            void setSolarJulianDay(final Long solarJulianDay) {
                setSolJd(solarJulianDay);
            }

            // ---------------------------------------------------------------------------------------------------------
            @NotBlank
            @XmlElement
            private String lunYear;

            @NotBlank
            @XmlElement
            private String lunMonth;

            @NotBlank
            @XmlElement
            private String lunDay;

            @Pattern(regexp = PATTERN_REGEXP_NORMAL_OR_LEAP)
            @NotNull
            @XmlElement
            private String lunLeapmonth;

            @XmlElement
            private int lunNday;

            @NotBlank
            @XmlElement
            private String lunSecha;

            @Nullable
            @XmlElement(required = false)
            private String lunWolgeon;

            @NotBlank
            @XmlElement
            private String lunIljin;

            // ---------------------------------------------------------------------------------------------------------
            @NotBlank
            @XmlElement
            private String solYear;

            @NotBlank
            @XmlElement
            private String solMonth;

            @NotBlank
            @XmlElement
            private String solDay;

            @Pattern(regexp = PATTERN_REGEXP_NORMAL_OR_LEAP)
            @NotNull
            @XmlElement
            private String solLeapyear;

            @Pattern(regexp = "[\uc6d4\ud654\uc218\ubaa9\uae08\ud1a0\uc77c]")
            @NotNull
            @XmlElement
            private String solWeek;

            @Positive
            @NotNull
            @XmlElement
            private Long solJd; // julian day, 율리우스일
        }

        // -------------------------------------------------------------------------------------------------------------
        @Override
        public String toString() {
            return super.toString() + '{'
                   + "items=" + items
                   + "}";
        }

        // ------------------------------------------------------------------------------------------------------- items
        public List<Item> getItems() {
            if (items == null) {
                items = new ArrayList<>();
            }
            return items;
        }

        public boolean isLastPage() {
            return numOfRows * pageNo >= totalCount;
        }

        // -------------------------------------------------------------------------------------------------------------
        @XmlElementWrapper
        @XmlElement(name = "item")
        private List<@Valid @NotNull Item> items;

        @Positive
        @XmlElement(required = true)
        private int numOfRows;

        @Positive
        @XmlElement(required = true)
        private int pageNo;

        @PositiveOrZero
        @XmlElement(required = true)
        private int totalCount;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return super.toString() + '{'
               + "header=" + header
               + ",body=" + body
               + '}';
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Valid
    @NotNull
    @XmlElement(required = true)
    private Header header;

    @Valid
    @NotNull
    @XmlElement(required = true)
    private Body body;
}
