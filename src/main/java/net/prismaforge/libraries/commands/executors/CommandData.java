package net.prismaforge.libraries.commands.executors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.PrismaCommand;
import net.prismaforge.libraries.commands.annotations.Command;
import net.prismaforge.libraries.commands.utility.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class CommandData {
    Object adapter;
    String name;
    String description;
    String usage;
    String[] aliases;
    boolean tabComplete;
    PrismaCommand listener;
    List<SubCommandData> subCommands;
    List<TabCompletionData> placeholders;

    public CommandData(@NonNull Object adapter, @NonNull Command command) {
        this.adapter = adapter;
        this.name = command.name();
        this.usage = command.usage();
        this.aliases = command.aliases();
        this.description = command.description();
        this.tabComplete = command.tabComplete();
        this.subCommands = new LinkedList<>();
        this.placeholders = new ArrayList<>();
        this.listener = new PrismaCommand(this);
    }

    @NonNull
    public List<SubCommandData> getSubCommandsSafe() {
        return new ArrayList<>(this.subCommands);
    }

    @NonNull
    public CommandData addSubCommand(@NonNull SubCommandData subCommand) {
        this.subCommands.add(subCommand);
        Collections.sort(this.subCommands);
        return this;
    }

    @NonNull
    public CommandData removeSubCommand(@NonNull SubCommandData subCommand) {
        this.subCommands.remove(subCommand);
        Collections.sort(this.subCommands);
        return this;
    }

    @NonNull
    public Optional<SubCommandData> findSubCommand(@NonNull String[] subCommands) {
        for (SubCommandData subCommandData : this.subCommands) {
            String[] commands = subCommandData.getArgs();
            if (CommandUtils.isMatching(commands, subCommands)) {
                return Optional.of(subCommandData);
            }
        }
        if (subCommands.length != 0) {
            return findSubCommand(new String[0]);
        }
        return Optional.empty();
    }

    public void addPlaceholder(@NonNull TabCompletionData placeholder) {
        this.placeholders.add(placeholder);
    }

    public void removePlaceholder(@NonNull TabCompletionData placeholder) {
        this.placeholders.remove(placeholder);
    }

    @NonNull
    public Optional<TabCompletionData> findPlaceholderByName(@NonNull String name) {
        for (TabCompletionData placeholderData : this.placeholders)
            if (placeholderData.getName().equals(name))
                return Optional.of(placeholderData);
        return Optional.empty();
    }

    @NonNull
    public Optional<TabCompletionData> findPlaceholderByArg(@NonNull String arg) {
        String holder = CommandUtils.getPlaceholder(arg);
        return (holder == null) ? Optional.empty() : this.findPlaceholderByName(holder);
    }

    public void register() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            org.bukkit.command.Command command = commandMap.getCommand(this.name);

            if (command != null && command.isRegistered())
                return;
            commandMap.register(this.name, this.listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void unregister() {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");

            commandMap.setAccessible(true);
            knownCommands.setAccessible(true);

            ((Map<String, org.bukkit.command.Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(this.name);
            this.listener.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}