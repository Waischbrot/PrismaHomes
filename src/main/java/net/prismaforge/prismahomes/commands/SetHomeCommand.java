package net.prismaforge.prismahomes.commands;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.annotations.Command;
import net.prismaforge.libraries.commands.annotations.SubCommand;
import net.prismaforge.libraries.commands.annotations.TabCompletion;
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

    public SetHomeCommand(final PrismaHomes plugin) {
        this.plugin = plugin;
    }

    @SubCommand(args = "%name%")
    public void onSetHome(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;
        final DataPlayer data = PrismaHomes.STORAGE().get(player.getUniqueId());

        //check if has slots
        final int slots = SlotsUtil.availableSlots(player);
        if (data.countHomes() >= slots) {
            player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.ERROR_NO_SLOTS.translate()));
            return;
        }

        final String key = args[0];
        //check if name meets naming conventions (azAZ09-)
        if (!key.matches("^[A-Za-z0-9_.]+$") || key.length() > 20) {
            player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.ERROR_INVALID_NAME.translate()));
            return;
        }

        //check if already has a home with this name
        for (final DataHome home : data.homes()) {
            if (key.equalsIgnoreCase(home.key())) {
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.ERROR_DUPLICATE_HOME.translate()));
                return;
            }
        }

        //create
        final Location location = player.getLocation();
        final DataHome home = new DataHome(key, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        CompletableFuture.runAsync(() -> {
            player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.SUCCESS_NEW_HOME.translate(s -> s.replaceAll("%name%", home.displayName()))));
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
        PrismaHomes.STORAGE().save(data);
    }
}