package net.prismaforge.libraries.strings;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F\\d]{6}");

    @NonNull
    public static String colorString(@NonNull String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @NonNull
    public static Color colorFromHex(final @NonNull String hex) {
        return Color.fromRGB(
                Integer.valueOf(hex.substring(1, 3),16),
                Integer.valueOf(hex.substring(3, 5),16),
                Integer.valueOf(hex.substring(5, 7),16));
    }

    @NonNull
    public static String randomHex() {
        Random random = new Random();
        int i = random.nextInt(0xffffff + 1);
        return String.format("#%06x", i);
    }
}
