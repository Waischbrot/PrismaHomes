package net.prismaforge.libraries.commands.executors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.annotations.TabCompletion;
import net.prismaforge.libraries.reflection.ReflectionUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class TabCompletionData {
    CommandData commandData;
    Method method;
    String name;
    String permission;

    public TabCompletionData(@NonNull CommandData commandData, @NonNull Method method, @NonNull TabCompletion placeholder) {
        this.commandData = commandData;
        this.method = method;
        this.name = placeholder.name();
        this.permission = placeholder.permission();
    }

    @Nullable
    public List<String> getValues(final CommandSender sender) {
        if (method.getParameterCount() == 0) {
            return ReflectionUtil.invoke(this.commandData.getAdapter(), this.method);
        }
        if (method.getParameterTypes()[0].equals(Player.class) && sender instanceof final Player player) {
            return ReflectionUtil.invoke(this.commandData.getAdapter(), this.method, player);
        }
        return new ArrayList<>();
    }
}
