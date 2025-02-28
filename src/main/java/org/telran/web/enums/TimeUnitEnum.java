package org.telran.web.enums;


public enum TimeUnitEnum {
    HOURS(12, "hours"),
    DAYS(24, "days"),
    WEEKS(24 * 7, "weeks"),
    MONTHS(24 * 30, "months"),
    YEARS(24 * 365, "year");

    private final int value;

    private final String unit;

    TimeUnitEnum(int value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return unit + (value > 1 ? "s" : "");
    }
}