package net.prismaforge.prismahomes.commands;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.commands.annotations.Command;
import net.prismaforge.libraries.commands.annotations.SubCommand;
import net.prismaforge.libraries.commands.annotations.TabCompletion;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.storage.DataHome;
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
    LoadingCache<UUID, List<String>> nameCache;

    public HomeCommand(final PrismaHomes plugin) {
        this.plugin = plugin;
        this.nameCache = CacheBuilder.newBuilder() //build loading cache for insane fast tabcompletion
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public List<String> load(final UUID key) throws Exception {
                        final var temp = PrismaAPI.getSurvivalHandler().fetchHazel(key);
                        if (temp.isEmpty()) return Collections.emptyList();
                        final SurvivalPlayer data = temp.get();
                        final List<String> homeKeys = new ArrayList<>();
                        data.homes().forEach(dataHome -> homeKeys.add(dataHome.key()));
                        return homeKeys;
                    }
                });
    }

    @SubCommand()
    public void onDefault(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return; //check if this is a player

        //load data
        final var temp = PrismaAPI.getSurvivalHandler().fetchHazel(player.getUniqueId());
        if (temp.isEmpty()) {
            MessageUtil.message(player, LangKey.DATA_ERROR, LangKey.PREFIX_SURVIVAL, s -> s.replaceAll("%player%", player.getName()));
            return;
        }
        final SurvivalPlayer data = temp.get();

        new ListHomesMenu(player, data, PlayerUtil.getLanguage(player.getUniqueId())).open();
    }

    @SubCommand(args = {"%names%"})
    public void onDirectHome(final CommandSender sender, final String[] args) {
        if (!(sender instanceof final Player player)) return;
        final String target = args[0];

        //load data
        final var temp = PrismaAPI.getSurvivalHandler().fetchHazel(player.getUniqueId());
        if (temp.isEmpty()) {
            MessageUtil.message(player, LangKey.DATA_ERROR, LangKey.PREFIX_SURVIVAL, s -> s.replaceAll("%player%", player.getName()));
            return;
        }
        final SurvivalPlayer data = temp.get();

        //loop through homes
        for (final DataHome home : data.homes()) {
            if (home.key().equalsIgnoreCase(target)) {
                player.teleport(new Location(Bukkit.getWorld(home.world()), home.x(), home.y(), home.z(), home.yaw(), home.pitch()));
                return;
            }
        }
        MessageUtil.message(player, LangKey.HOMES_NO_HOME_FOUND, LangKey.PREFIX_HOMES);
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