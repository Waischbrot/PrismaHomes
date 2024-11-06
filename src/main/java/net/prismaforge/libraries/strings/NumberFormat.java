package net.prismaforge.libraries.strings;

import lombok.NonNull;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class NumberFormat {
    private static final DecimalFormat SWISS_FORMAT = new DecimalFormat("###,###");
    private static final NavigableMap<Long, String> SUFFIXES = new TreeMap<>();
    static {
        SUFFIXES.put(1_000L, "k");
        SUFFIXES.put(1_000_000L, "m");
        SUFFIXES.put(1_000_000_000L, "b");
        SUFFIXES.put(1_000_000_000_000L, "t");
        SUFFIXES.put(1_000_000_000_000_000L, "aa");
        SUFFIXES.put(1_000_000_000_000_000_000L, "ab");
    }

    @NonNull
    public static String suffix(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return suffix(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + suffix(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = SUFFIXES.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10d);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    @NonNull
    public static String apostrophs(final long value) {
        return SWISS_FORMAT.format(value).replaceAll(",", "'");
    }

    @NonNull
    public static String commas(long value) {
        return String.format("%,d", value);
    }

    @NonNull
    public static String twoDecimals(double value) {
        return String.format("%.2f", value);
    }

    @NonNull
    public static String threeDecimals(double value) {
        return String.format("%.3f", value);
    }

    @NonNull
    public static String fourDecimals(double value) {
        return String.format("%.4f", value);
    }

    @NonNull
    public static String roman(int value) {
        return "I".repeat(value)
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }
}
