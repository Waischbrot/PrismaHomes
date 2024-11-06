package net.prismaforge.prismahomes.commands;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.annotations.Command;
import net.prismaforge.libraries.commands.annotations.SubCommand;
import net.prismaforge.libraries.commands.annotations.TabCompletion;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.libraries.strings.ColorUtil;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.storage.DataHome;
import net.prismaforge.prismahomes.storage.DataPlayer;
import net.prismaforge.prismahomes.utility.LangKey;
import net.prismaforge.prismahomes.utility.SlotsUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Command(name = "sethome")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class SetHomeCommand {
    PrismaHomes plugin;
    Config config;

    public SetHomeCommand(final PrismaHomes plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
    }

    @SubCommand(args = "%name%")
    public void onSetHome(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;
        final DataPlayer data = plugin.getStorageHandler().get(player.getUniqueId());

        //check if has slots
        final int slots = SlotsUtil.availableSlots(player);
        if (data.homes().size() >= slots) {
            player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.ERROR_NO_SLOTS.translate(config)));
            return;
        }

        final String key = args[0];
        //check if name meets naming conventions (azAZ09-)
        if (!key.matches("^[A-Za-z0-9_.]+$") || key.length() > 20) {
            player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.ERROR_INVALID_NAME.translate(config)));
            return;
        }

        //check if already has a home with this name
        for (final DataHome home : data.homes()) {
            if (key.equalsIgnoreCase(home.key())) {
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.ERROR_DUPLICATE_HOME.translate(config)));
                return;
            }
        }

        //create
        final Location location = player.getLocation();
        final DataHome home = new DataHome(key, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        CompletableFuture.runAsync(() -> {
            player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.SUCCESS_NEW_HOME.translate(config, s -> s.replaceAll("%name%", home.displayName()))));
            data.homes().add(home);
            saveSecure(data);
        });
    }

    @TabCompletion(name = "name")
    @NonNull
    public List<String> getName() {
        return List.of("<Name>");
    }

    private void saveSecure(final DataPlayer data) {
        this.plugin.getStorageHandler().save(data);
    }
}