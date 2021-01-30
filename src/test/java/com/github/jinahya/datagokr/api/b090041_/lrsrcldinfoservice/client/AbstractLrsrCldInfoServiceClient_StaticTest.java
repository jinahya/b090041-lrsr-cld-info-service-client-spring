package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import org.junit.jupiter.api.Test;

import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoField;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractLrsrCldInfoServiceClient_StaticTest {

    /**
     * Asserts {@link AbstractLrsrCldInfoServiceClient#MIN_DAY_OF_MONTH_LUNAR} is equal to the minimum value of {@link
     * ChronoField#DAY_OF_MONTH} from {@link IsoChronology}.
     */
    @SuppressWarnings({"java:S3415"})
    @Test
    void MIN_DAY_OF_MONTH_LUNAR_IsEqualTo_MinimumDayOfMonthFromIsoChronology() {
        final long expected = IsoChronology.INSTANCE.range(ChronoField.DAY_OF_MONTH).getMinimum();
        assertThat(AbstractLrsrCldInfoServiceClient.MIN_DAY_OF_MONTH_LUNAR)
                .isEqualTo((int) expected);
    }

    /**
     * Asserts {@link AbstractLrsrCldInfoServiceClient#MAX_DAY_OF_MONTH_LUNAR} is less than or equal to the maximum
     * value of {@link ChronoField#DAY_OF_MONTH} from {@link IsoChronology}.
     */
    @SuppressWarnings({"java:S3415"})
    @Test
    void MIN_DAY_OF_MONTH_LUNAR_IsEqualToOrLessThan_MaximumDayOfMonthFromIsoChronology() {
        final long other = IsoChronology.INSTANCE.range(ChronoField.DAY_OF_MONTH).getMaximum();
        assertThat(AbstractLrsrCldInfoServiceClient.MAX_DAY_OF_MONTH_LUNAR)
                .isLessThanOrEqualTo((int) other);
    }

    /**
     * Asserts {@link AbstractLrsrCldInfoServiceClient#MIN_DAY_OF_MONTH_SOLAR} is equal to the minimum value of {@link
     * ChronoField#DAY_OF_MONTH} from {@link IsoChronology}.
     */
    @SuppressWarnings({"java:S3415"})
    @Test
    void MIN_DAY_OF_MONTH_SOLAR_IsEqualTo_MinimumDayOfMonthFromIsoChronology() {
        final long expected = IsoChronology.INSTANCE.range(ChronoField.DAY_OF_MONTH).getMinimum();
        assertThat(AbstractLrsrCldInfoServiceClient.MIN_DAY_OF_MONTH_SOLAR)
                .isEqualTo((int) expected);
    }

    /**
     * Asserts {@link AbstractLrsrCldInfoServiceClient#MAX_DAY_OF_MONTH_SOLAR} is equal to the maximum value of {@link
     * ChronoField#DAY_OF_MONTH} from {@link IsoChronology}.
     */
    @SuppressWarnings({"java:S3415"})
    @Test
    void MIN_DAY_OF_MONTH_SOLAR_IsEqualToOrLessThan_MaximumDayOfMonthFromIsoChronology() {
        final long other = IsoChronology.INSTANCE.range(ChronoField.DAY_OF_MONTH).getMaximum();
        assertThat(AbstractLrsrCldInfoServiceClient.MAX_DAY_OF_MONTH_SOLAR)
                .isEqualTo((int) other);
    }
}