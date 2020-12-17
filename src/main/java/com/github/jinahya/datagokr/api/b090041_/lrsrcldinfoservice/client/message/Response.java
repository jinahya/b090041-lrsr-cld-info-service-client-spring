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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        public static class Item {

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

            // ------------------------------------------------------------------------------------------------- lunYear

            // ------------------------------------------------------------------------------------------------ lunMonth
            public Month getLunMonthAsMonth() {
                return ofNullable(getLunMonth()).map(Integer::parseInt).map(Month::of).orElse(null);
            }

            // -------------------------------------------------------------------------------------------------- lunDay
            public Integer getLunDayAsDayOfMonth() {
                return ofNullable(getLunDay()).map(Integer::parseInt).orElse(null);
            }

            // ----------------------------------------------------------------------------- lunYear / lunMonth / lunDay
            public LocalDate getLunYearMonthDayAsLocalDate() {
                return LocalDate.of(Integer.parseInt(getLunYear()), Integer.parseInt(getLunMonth()),
                                    Integer.parseInt(getLunDay()));
            }

            void setLunYearMonthDayAsLocalDate(final LocalDate lunYearMonthDayAsLocalDate) {
                if (lunYearMonthDayAsLocalDate == null) {
                    setLunYear(null);
                    setLunMonth(null);
                    setLunDay(null);
                    //setLunLeapmonth(null);
                    return;
                }
                setLunYear(YEAR_FORMATTER.format(lunYearMonthDayAsLocalDate));
                setLunMonth(MONTH_FORMATTER.format(lunYearMonthDayAsLocalDate));
                setLunDay(DAY_FORMATTER.format(lunYearMonthDayAsLocalDate));
            }

            // -------------------------------------------------------------------------------------------- lunLeapmonth
            public boolean getLunLeapmonthAsBoolean() {
                return LEAP.equals(getLunLeapmonth());
            }

            // ------------------------------------------------------------------------------------------------ lunSecha

            // ---------------------------------------------------------------------------------------------- lunWolgeon

            // -----------------------------------------------------------------------------------------------  lunIljin

            // ----------------------------------------------------------------------------- solYear / solMonth / solDay
            public LocalDate getSolYearMonthDayAsLocalDate() {
                return LocalDate.of(Integer.parseInt(getSolYear()), Integer.parseInt(getSolMonth()),
                                    Integer.parseInt(getSolDay()));
            }

            void setSolYearMonthDayAsLocalDate(final LocalDate solYearMonthDayAsLocalDate) {
                if (solYearMonthDayAsLocalDate == null) {
                    setSolYear(null);
                    setSolMonth(null);
                    setSolDay(null);
                    setSolWeek(null);
                    setSolLeapyear(null);
                    setSolJd(null);
                    return;
                }
                setSolYear(YEAR_FORMATTER.format(solYearMonthDayAsLocalDate));
                setSolMonth(MONTH_FORMATTER.format(solYearMonthDayAsLocalDate));
                setSolDay(DAY_FORMATTER.format(solYearMonthDayAsLocalDate));
                setSolWeek(WEEK_FORMATTER.format(solYearMonthDayAsLocalDate));
                setSolLeapyear(solYearMonthDayAsLocalDate.isLeapYear() ? LEAP : NORMAL);
                setSolJd(solYearMonthDayAsLocalDate.getLong(JulianFields.JULIAN_DAY));
            }

            // --------------------------------------------------------------------------------------------- solLeapyear
            public boolean getSolLeapyearAsBoolean() {
                return LEAP.equals(getSolLeapyear());
            }

            @AssertTrue
            private boolean isSolLeapyearValid() {
                return getSolYearMonthDayAsLocalDate().isLeapYear() == getSolLeapyearAsBoolean();
            }

            // ------------------------------------------------------------------------------------------------- solWeek
            @NotNull
            public DayOfWeek getSolWeekAsDayOfWeek() {
                return DayOfWeek.of(WEEK_FORMATTER.parse(getSolWeek()).get(ChronoField.DAY_OF_WEEK));
            }

            @AssertTrue
            private boolean isSolWeekValid() {
                return WEEK_FORMATTER.format(getSolYearMonthDayAsLocalDate().getDayOfWeek()).equals(getSolWeek());
            }

            // --------------------------------------------------------------------------------------------------- solJd
            @AssertTrue
            private boolean isSolJdValid() {
                return solJd == getSolYearMonthDayAsLocalDate().getLong(JulianFields.JULIAN_DAY);
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

        // -------------------------------------------------------------------------------------------------------------
        @XmlElementWrapper
        @XmlElement(name = "item")
        private List<@Valid @NotNull Item> items;
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
