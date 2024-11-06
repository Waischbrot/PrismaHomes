package net.prismaforge.prismahomes.commands;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import net.prismaforge.prismahomes.ui.ListHomesMenu;
import net.prismaforge.prismahomes.utility.LangKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Command(name = "home", aliases = {"homes"})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class HomeCommand {
    PrismaHomes plugin;
    Config config;
    LoadingCache<UUID, List<String>> nameCache;

    public HomeCommand(final PrismaHomes plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.nameCache = CacheBuilder.newBuilder() //build loading cache for insane fast tabcompletion
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public List<String> load(final UUID key) throws Exception {
                        final DataPlayer data = plugin.getStorageHandler().get(key);
                        final List<String> homeKeys = new ArrayList<>();
                        data.homes().forEach(dataHome -> homeKeys.add(dataHome.key()));
                        return homeKeys;
                    }
                });
    }

    @SubCommand()
    public void onDefault(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return; //check if this is a player
        final DataPlayer data = plugin.getStorageHandler().get(player.getUniqueId());
        new ListHomesMenu(player, plugin, data).open();
    }

    @SubCommand(args = {"%names%"})
    public void onDirectHome(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;
        final String target = args[0];
        final DataPlayer data = plugin.getStorageHandler().get(player.getUniqueId());

        //loop through homes
        for (final DataHome home : data.homes()) {
            if (home.key().equalsIgnoreCase(target)) {
                player.teleport(new Location(Bukkit.getWorld(home.world()), home.x(), home.y(), home.z(), home.yaw(), home.pitch()));
                return;
            }
        }

        player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.HOMES_NO_HOME_FOUND.translate(config)));
    }

    @TabCompletion(name = "names")
    @NonNull
    public List<String> getHomeNames(final Player player) {
        try {
            return nameCache.get(player.getUniqueId());
        } catch (ExecutionException e) {
            return Collections.emptyList();
        }
    }
}