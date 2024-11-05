package net.prismaforge.libraries.commands;

import lombok.NonNull;
import net.prismaforge.libraries.commands.executors.CommandData;
import net.prismaforge.libraries.commands.executors.SubCommandData;
import net.prismaforge.libraries.commands.executors.TabCompletionData;
import net.prismaforge.libraries.commands.utility.CommandUtils;
import net.prismaforge.libraries.reflection.ReflectionUtil;
import net.prismaforge.libraries.strings.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.util.StringUtil;

import java.util.*;

public final class PrismaCommand extends BukkitCommand {
    private final CommandData commandData;

    public PrismaCommand(@NonNull CommandData commandData) {
        super(commandData.getName(), commandData.getDescription(), commandData.getUsage(), Arrays.asList(commandData.getAliases()));
        this.commandData = commandData;
    }

    @Override
    public boolean execute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
        SubCommandData subCommandData = this.commandData.findSubCommand(args).orElse(null);
        if (subCommandData == null) {
            sender.sendMessage(ColorUtil.colorString(this.commandData.getUsage()));
            return false;
        } else if (!subCommandData.getPermission().isEmpty()) {
            if (!CommandUtils.hasPermission(sender, subCommandData.getPermission())) {
                sender.sendMessage(ColorUtil.colorString(subCommandData.getPermissionMessage()));
                return false;
            }
        }
        ReflectionUtil.invoke(this.commandData.getAdapter(), subCommandData.getMethod(), sender, args);
        return false;
    }

    @NonNull
    @Override
    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull String alias, @NonNull String[] args) {
        // Commands ohne Tabcompletion herausfiltern
        if (!this.commandData.isTabComplete())
            return new ArrayList<>();
        // Benötigte Variablen deklarieren..
        String lastArg = args[args.length - 1];
        Set<String> tabComplete = new LinkedHashSet<>();
        Set<String> tabCompleteBefore = new LinkedHashSet<>();
        List<String> enteredArgs = new LinkedList<>(Arrays.asList(args));
        List<SubCommandData> subCommandData = this.commandData.getSubCommandsSafe();
        // Letztes Argument entfernen
        enteredArgs.remove(enteredArgs.size() - 1);
        // Wurde unterwegs ein spezieller Weg (Subcommand) eingeschlagen?
        int i = 0;
        for (; i < enteredArgs.size(); i++) {
            String arg = enteredArgs.get(i);
            for (SubCommandData subCommand : new ArrayList<>(subCommandData)) {
                String[] subArgs = subCommand.getArgs();
                if (subArgs.length <= i || !(subArgs[i].equals(arg) || CommandUtils.hasPlaceholder(subArgs[i])))
                    subCommandData.remove(subCommand);
            }
        }
        // Subcommands durchgehen und Argumente listen
        for (SubCommandData subCommand : subCommandData) {
            String[] subArgs = subCommand.getArgs();
            int subArgLen = subArgs.length;
            if (subArgLen != 0 && subArgLen > i) {
                tabCompleteBefore.add(subArgs[i]);
            }
        }
        // TabCompletions ersetzen in den Argumenten
        for (String tab : new HashSet<>(tabCompleteBefore)) {
            TabCompletionData tabCompletionData = this.commandData.findPlaceholderByArg(tab).orElse(null);
            if (tabCompletionData != null) {
                if (CommandUtils.hasPermission(sender, tabCompletionData.getPermission()))
                    tabComplete.addAll(tabCompletionData.getValues(sender)); //this might be a problem -> what happens if this returns null?
                tabCompleteBefore.remove(tab);
            } else {
                tabComplete.add(tab);
            }
        }
        tabComplete.addAll(tabCompleteBefore);
        // Endgültige Liste aller Tabcompletes zusammenstellen!
        Set<String> tabCompleteList = new LinkedHashSet<>();
        tabComplete.stream().filter(tab -> tab.equals(lastArg)).forEach(tabCompleteList::add);
        tabComplete.stream().filter(tab -> tab.startsWith(lastArg)).forEach(tabCompleteList::add);
        tabCompleteList.addAll(tabComplete);
        // Keine statischen Argumente (dynamisch zugeschnitten auf Spieler-Input)
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(lastArg, new ArrayList<>(tabCompleteList), completions);
        Collections.sort(completions);
        return completions;
    }
}
