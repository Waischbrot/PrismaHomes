package net.prismaforge.libraries.commands.executors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.annotations.SubCommand;

import java.lang.reflect.Method;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class SubCommandData implements Comparable<SubCommandData> {
    CommandData commandData;
    Method method;
    String[] args;
    String permission;
    String permissionMessage;

    public SubCommandData(@NonNull CommandData commandData, @NonNull Method method, @NonNull SubCommand subCommand) {
        this.commandData = commandData;
        this.method = method;
        this.args = subCommand.args();
        this.permission = subCommand.permission();
        this.permissionMessage = subCommand.permissionMessage();
    }

    @Override
    public int compareTo(@NonNull SubCommandData that) {
        return Integer.compare(that.args.length, this.args.length);
    }
}
