package org.telran.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.temporal.ChronoUnit;

/**
 * Enum representing different time units that can be used for reporting purposes.
 * This enum maps time units to {@link "ChronoUnit"} for precise temporal calculations.
 */
public enum TimeUnitEnum {

    /** Represents hours as a time unit. */
    HOURS(ChronoUnit.HOURS),

    /** Represents days as a time unit. */
    DAYS(ChronoUnit.DAYS),

    /** Represents weeks as a time unit. */
    WEEKS(ChronoUnit.WEEKS),

    /** Represents months as a time unit. */
    MONTHS(ChronoUnit.MONTHS),

    /** Represents years as a time unit. */
    YEARS(ChronoUnit.YEARS);

    private final ChronoUnit chronoUnit;

    /**
     * Constructs a TimeUnitEnum with the corresponding {@link "ChronoUnit"}.
     *
     * @param chronoUnit The corresponding {@link "ChronoUnit}" value.
     */
    TimeUnitEnum(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
    }

    /**
     * Returns the string representation of the enum constant.
     *
     * @return The name of the enum constant (e.g., "HOURS", "DAYS", "WEEKS").
     */
    @JsonValue
    public String getValue() {
        return name();
    }

    /**
     * Converts this enum to its corresponding {@link "ChronoUnit"} value.
     *
     * @return The corresponding {@link "ChronoUnit"} value.
     */
    public ChronoUnit toChronoUnit() {
        return chronoUnit;
    }

    /**
     * Converts a string to the corresponding TimeUnitEnum constant.
     *
     * @param text The string value to be converted.
     * @return The corresponding TimeUnitEnum constant.
     * @throws IllegalArgumentException If the input string does not match any enum constant.
     */
    @JsonCreator
    public static TimeUnitEnum fromString(String text) {
        for (TimeUnitEnum unit : TimeUnitEnum.values()) {
            if (unit.name().equalsIgnoreCase(text)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid time unit: " + text + ". Allowed values: HOURS, DAYS, WEEKS, MONTHS, YEARS");
    }
}