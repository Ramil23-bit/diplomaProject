package org.telran.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing the grouping criteria for revenue reports.
 * This enum defines possible values that can be used to group revenue data.
 */
public enum GroupBy {

    /** Group revenue by product category. */
    CATEGORY("category"),

    /** Group revenue by date. */
    DATE("date"),

    /** Group revenue by customer. */
    CUSTOMER("customer");

    private final String value;

    /**
     * Constructs a GroupBy enum with the specified value.
     *
     * @param value The string representation of the enum value.
     */
    GroupBy(String value) {
        this.value = value;
    }

    /**
     * Returns the string value associated with this enum constant.
     *
     * @return The string representation of the enum.
     */
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Converts a string into the corresponding GroupBy enum constant.
     *
     * @param text The string value to be converted.
     * @return The corresponding GroupBy enum constant.
     * @throws IllegalArgumentException If the input text does not match any enum constant.
     */
    @JsonCreator
    public static GroupBy fromString(String text) {
        for (GroupBy groupBy : GroupBy.values()) {
            if (groupBy.value.equalsIgnoreCase(text)) {
                return groupBy;
            }
        }
        throw new IllegalArgumentException("Invalid groupBy value: " + text);
    }
}