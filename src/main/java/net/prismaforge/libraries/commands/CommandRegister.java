package net.prismaforge.libraries.commands;

import lombok.NonNull;
import net.prismaforge.libraries.commands.annotations.Command;
import net.prismaforge.libraries.commands.annotations.SubCommand;
import net.prismaforge.libraries.commands.annotations.TabCompletion;
import net.prismaforge.libraries.commands.executors.CommandData;
import net.prismaforge.libraries.commands.executors.SubCommandData;
import net.prismaforge.libraries.commands.executors.TabCompletionData;

import java.lang.reflect.Method;

public final class CommandRegister {

    public static void register(@NonNull Object... adapters) {
        for (Object adapter : adapters) {
            Command command = adapter.getClass().getAnnotation(Command.class);
            if (command == null)
                continue;
            CommandData commandData = new CommandData(adapter, command);
            commandData.register();
            for (Method method : adapter.getClass().getDeclaredMethods()) {
                SubCommand commandPart = method.getAnnotation(SubCommand.class);
                if (commandPart != null) {
                    SubCommandData subCommandData = new SubCommandData(commandData, method, commandPart);
                    commandData.addSubCommand(subCommandData);
                }
                TabCompletion tabCompletion = method.getAnnotation(TabCompletion.class);
                if (tabCompletion != null) {
                    TabCompletionData tabCompletionData = new TabCompletionData(commandData, method, tabCompletion);
                    commandData.addPlaceholder(tabCompletionData);
                }
            }
        }
    }
}
