package net.prismaforge.libraries.commands.utility;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandUtils {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(?<=%)(?<placeholder>.+?)(?=%)");

    public static boolean isMatching(final @NonNull String[] args1, final @NonNull String[] args2) {
        if (args2.length != args1.length)
            return false;
        for (int i = 0; i < args1.length; i++)
            if (!(args1[i].equalsIgnoreCase(args2[i]) || CommandUtils.hasPlaceholder(args1[i])))
                return false;
        return true;
    }

    @Nullable
    public static String getPlaceholder(@NonNull String arg) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(arg);
        return matcher.find() ? matcher.group("placeholder") : null;
    }

    public static boolean hasPlaceholder(@NonNull String arg) {
        return PLACEHOLDER_PATTERN.matcher(arg).find();
    }

    public static boolean hasPermission(@NonNull CommandSender sender, @NonNull String permission) {
        return permission.isEmpty() ||
                sender.isOp() ||
                sender.hasPermission("*") ||
                sender.hasPermission(permission) ||
                sender instanceof ConsoleCommandSender;
    }
}
