package net.prismaforge.prismahomes.commands;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.annotations.Command;
import net.prismaforge.libraries.commands.annotations.SubCommand;
import net.prismaforge.libraries.commands.annotations.TabCompletion;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.utility.SlotsUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Command(name = "sethome")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class SetHomeCommand {
    PrismaHomes plugin;

    @SubCommand(args = "%name%")
    public void onSetHome(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;

        //load data
        final var temp = PrismaAPI.getSurvivalHandler().fetchHazel(player.getUniqueId());
        if (temp.isEmpty()) {
            MessageUtil.message(player, LangKey.DATA_ERROR, LangKey.PREFIX_SURVIVAL, s -> s.replaceAll("%player%", player.getName()));
            return;
        }
        final SurvivalPlayer data = temp.get();

        //check if has slots
        final int slots = SlotsUtil.availableSlots(player);
        if (data.homes().size() >= slots) {
            MessageUtil.message(player, LangKey.HOMES_NO_SLOTS, LangKey.PREFIX_HOMES);
            return;
        }

        final String key = args[0];
        //check if name meets naming conventions (azAZ09-)
        if (!key.matches("^[A-Za-z0-9_.]+$") || key.length() > 20) {
            MessageUtil.message(player, LangKey.HOMES_INVALID_NAME, LangKey.PREFIX_HOMES);
            return;
        }

        //check if already has a home with this name
        for (final DataHome home : data.homes()) {
            if (key.equalsIgnoreCase(home.key())) {
                MessageUtil.message(player, LangKey.HOMES_HOME_ALREADY_PRESENT, LangKey.PREFIX_HOMES);
                return;
            }
        }

        //create
        final Location location = player.getLocation();
        final DataHome home = new DataHome(key, location.getWorld().getName(), location.x(), location.y(), location.z(), location.getYaw(), location.getPitch());
        CompletableFuture.runAsync(() -> {
            MessageUtil.message(player, LangKey.HOMES_NEW_HOME_CREATED, LangKey.PREFIX_HOMES, s -> s.replaceAll("%name%", home.displayName()));
            data.homes().add(home);
            PrismaAPI.getSurvivalHandler().updateHazel(data);
        });
    }

    @TabCompletion(name = "name")
    @NonNull
    public List<String> getName() {
        return List.of("<Name>");
    }
}