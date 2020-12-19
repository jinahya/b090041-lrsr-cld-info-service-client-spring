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
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
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

            /**
             * A regular expression of {@code 갑을병정무기경신임계}. The value is {@value}.
             *
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%B2%9C%EA%B0%84">천간</a>
             */
            static final String PATTERN_REGEXP_천간
                    = "[\uac11\uc744\ubcd1\uc815\ubb34\uae30\uacbd\uc2e0\uc784\uacc4]";

            /**
             * A regular expression of {@code 자축인묘인사오미신유술해}. The value is {@value}.
             *
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%A7%80%EC%A7%80_(%EC%97%AD%EB%B2%95)">지지</a>
             */
            static final String PATTERN_REGEXP_지지
                    = "[\uc790\ucd95\uc778\ubb18\uc9c4\uc0ac\uc624\ubbf8\uc2e0\uc720\uc220\ud574]";

            /**
             * A regular expression for <a href="https://ko.wikipedia.org/wiki/%EA%B0%84%EC%A7%80">간지</a>. The value is
             * {@value}.
             *
             * @see <a href="https://ko.wikipedia.org/wiki/%EA%B0%84%EC%A7%80">간지</a>
             */
            static final String PATTERN_REGEXP_간지
                    = "(?<stemKore>" + PATTERN_REGEXP_천간 + ")(?<branchKore>" + PATTERN_REGEXP_지지 + ")";

            // ---------------------------------------------------------------------------------------------------------

            /**
             * A regular expression of {@code 甲乙丙丁戊己庚辛壬癸}. The value is {@value}.
             *
             * @see <a href="https://zh.wikipedia.org/wiki/%E5%A4%A9%E5%B9%B2">天干</a>
             */
            static final String PATTERN_REGEXP_天干
                    = "[\u7532\u4e59\u4e19\u4e01\u620a\u5df1\u5e9a\u8f9b\u58ec\u7678]";

            /**
             * A regular expression for {@code 子丑寅卯辰巳午未申酉戌亥}. The value is {@value}.
             *
             * @see <a href="https://zh.wikipedia.org/wiki/%E5%9C%B0%E6%94%AF">地支</a>
             */
            static final String PATTERN_REGEXP_地支
                    = "[\u5b50\u4e11\u5bc5\u536f\u8fb0\u5df3\u5348\u672a\u7533\u9149\u620c\u4ea5]";

            /**
             * A regular expression for <a href="https://zh.wikipedia.org/wiki/%E5%B9%B2%E6%94%AF">干支</a>. The value is
             * {@value}.
             *
             * @see <a href="https://zh.wikipedia.org/wiki/%E5%B9%B2%E6%94%AF">干支</a>
             */
            static final String PATTERN_REGEXP_干支
                    = "(?<stemHans>" + PATTERN_REGEXP_天干 + ")(?<branchHans>" + PATTERN_REGEXP_地支 + ")";

            // ---------------------------------------------------------------------------------------------------------
            static final String PATTERN_REGEXP_간지_干支
                    = PATTERN_REGEXP_간지
                      + "\\("
                      + PATTERN_REGEXP_干支
                      + "\\)";

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
            }

            void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
                if (lunWolgeon != null && lunWolgeon.trim().isEmpty()) {
                    lunWolgeon = null;
                }
            }

            // ------------------------------------------------------------------------------------- lunYear / lunarYear
            public Year getLunarYear() {
                return ofNullable(getLunYear()).map(v -> Year.parse(v, YEAR_FORMATTER)).orElse(null);
            }

            void setLunarYear(final Year lunarYear) {
                setLunYear(ofNullable(lunarYear).map(YEAR_FORMATTER::format).orElse(null));
            }

            // ----------------------------------------------------------------------------------- lunMonth / lunarMonth
            public Month getLunarMonth() {
                return ofNullable(getLunMonth()).map(Integer::parseInt).map(Month::of).orElse(null);
            }

            void setLunarMonth(final Month lunarMonth) {
                setLunMonth(ofNullable(lunarMonth).map(MONTH_FORMATTER::format).orElse(null));
            }

            // -------------------------------------------------------------------------------- lunDay / lunarDayOfMonth
            public Integer getLunarDayOfMonth() {
                return ofNullable(getLunDay()).map(Integer::parseInt).orElse(null);
            }

            void setLunarDayOfMonth(final Integer lunarDayOfMonth) {
                setLunDay(ofNullable(lunarDayOfMonth).map(v -> format("%1$02d", v)).orElse(null));
            }

            // ------------------------------------------------------------------------------------------ lunarYearMonth
            public YearMonth getLunarYearMonth() {
                return YearMonth.of(getLunarYear().getValue(), getLunarMonth());
            }

            void setLunarYearMonth(final YearMonth lunarYearMonth) {
                setLunarYear(ofNullable(lunarYearMonth).map(YearMonth::getYear).map(Year::of).orElse(null));
                setLunarMonth(ofNullable(lunarYearMonth).map(YearMonth::getMonth).orElse(null));
            }

            // ------------------------------------------------------------------------------------------- lunarMonthDay
            public MonthDay getLunarMonthDay() {
                return MonthDay.of(getLunarMonth(), getLunarDayOfMonth());
            }

            void setLunarMonthDay(final MonthDay lunarMonthDay) {
                setLunarMonth(ofNullable(lunarMonthDay).map(MonthDay::getMonth).orElse(null));
                setLunarDayOfMonth(ofNullable(lunarMonthDay).map(MonthDay::getDayOfMonth).orElse(null));
            }

            // ----------------------------------------------------------------------------------------------- lunarDate
            public LocalDate getLunarDate() {
                return LocalDate.of(getLunarYear().getValue(), getLunarMonth(), getLunarDayOfMonth());
            }

            public void setLunarDate(final LocalDate lunarDate) {
                setLunarYear(ofNullable(lunarDate).map(Year::from).orElse(null));
                setLunarMonth(ofNullable(lunarDate).map(Month::from).orElse(null));
                setLunarDayOfMonth(ofNullable(lunarDate).map(LocalDate::getDayOfMonth).orElse(null));
            }

            // --------------------------------------------------------------------------- lunLeapmonth / lunarLeapMonth
            public Boolean getLunarLeapMonth() {
                return LEAP.equals(getLunLeapmonth());
            }

            @SuppressWarnings({"java:S5411"})
            public void setLunarLeapMonth(final Boolean lunarLeapMonth) {
                setLunLeapmonth(ofNullable(lunarLeapMonth).map(v -> v ? LEAP : NORMAL).orElse(null));
            }

            // ------------------------------------------------------------------------------------------------ lunSecha

            // ------------------------------------------------------------------------------------------------- 세차 / 歲次

            /**
             * Returns current value of {@code 세차} property.
             *
             * @return current value of {@code 세차} property.
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%84%B8%EC%B0%A8_(%EA%B0%84%EC%A7%80)">세차 (간지)</a>
             */
            public String get세차() {
                return ofNullable(getLunSecha()).map(s -> s.substring(1, 3)).orElse(null);
            }

            /**
             * Returns current value of {@code 歲次} property.
             *
             * @return current value of {@code 歲次} property.
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%84%B8%EC%B0%A8_(%EA%B0%84%EC%A7%80)">세차 (간지)</a>
             */
            public String get歲次() {
                return ofNullable(getLunSecha()).map(s -> s.substring(1, 3)).orElse(null);
            }

            // ---------------------------------------------------------------------------------------------- lunWolgeon

            // ------------------------------------------------------------------------------------------------- 월건 / 月建

            /**
             * Returns current value of {@code 월건} property.
             *
             * @return current value of {@code 월건} property.
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%9B%94%EA%B1%B4">월건</a>
             */
            public String get월건() {
                return ofNullable(getLunWolgeon()).map(s -> s.substring(1, 3)).orElse(null);
            }

            /**
             * Returns current value of {@code 月建} property.
             *
             * @return current value of {@code 月建} property.
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%9B%94%EA%B1%B4">월건</a>
             */
            public String get月建() {
                return ofNullable(getLunWolgeon()).map(s -> s.substring(1, 3)).orElse(null);
            }

            // -----------------------------------------------------------------------------------------------  lunIljin

            // ------------------------------------------------------------------------------------------------- 일진 / 日辰

            /**
             * Returns current value of {@code 일진} property.
             *
             * @return current value of {@code 일진} property.
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%9D%BC%EC%A7%84_(%EA%B0%84%EC%A7%80)">일진</a>
             */
            public String get일진() {
                return ofNullable(getLunWolgeon()).map(s -> s.substring(1, 3)).orElse(null);
            }

            /**
             * Returns current value of {@code 日辰} property.
             *
             * @return current value of {@code 日辰} property.
             * @see <a href="https://ko.wikipedia.org/wiki/%EC%9D%BC%EC%A7%84_(%EA%B0%84%EC%A7%80)">일진</a>
             */
            public String get日辰() {
                return ofNullable(getLunWolgeon()).map(s -> s.substring(1, 3)).orElse(null);
            }

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

            // ------------------------------------------------------------------------------------------ solarYearMonth
            public YearMonth getSolarYearMonth() {
                return YearMonth.of(getSolarYear().getValue(), getSolarMonth());
            }

            void setSolarYearMonth(final YearMonth solarYearMonth) {
                setSolarYear(ofNullable(solarYearMonth).map(YearMonth::getYear).map(Year::of).orElse(null));
                setSolarMonth(ofNullable(solarYearMonth).map(YearMonth::getMonth).orElse(null));
            }

            // ------------------------------------------------------------------------------------------- solarMonthDay
            public MonthDay getSolarMonthDay() {
                return MonthDay.of(getSolarMonth(), getSolarDayOfMonth());
            }

            void setSolarMonthDay(final MonthDay solarMonthDay) {
                setSolarMonth(ofNullable(solarMonthDay).map(MonthDay::getMonth).orElse(null));
                setSolarDayOfMonth(ofNullable(solarMonthDay).map(MonthDay::getDayOfMonth).orElse(null));
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
                setSolLeapyear(ofNullable(solarLeapYear).map(v -> v ? LEAP : NORMAL).orElse(null));
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

            @Pattern(regexp = PATTERN_REGEXP_간지_干支)
            @NotNull
            @XmlElement
            private String lunSecha;

            @Pattern(regexp = PATTERN_REGEXP_간지_干支)
            @Nullable
            @XmlElement(required = false)
            private String lunWolgeon;

            @Pattern(regexp = PATTERN_REGEXP_간지_干支)
            @NotNull
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
