package net.prismaforge.prismahomes.listener;

import net.prismaforge.prismahomes.PrismaHomes;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ChatListener implements Listener {
    private static final Map<Player, Consumer<String>> TASKS = new HashMap<>();
    private final PrismaHomes plugin;

    public ChatListener(PrismaHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChatInput(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (TASKS.containsKey(player)) {
            TASKS.remove(player).accept(event.getMessage());
            event.setCancelled(true);
        }
    }

    public static void addTask(Player player, Consumer<String> consumer) {
        TASKS.remove(player);
        TASKS.put(player, consumer);
    }

    public static void removeTask(final Player player) {
        TASKS.remove(player);
    }
}
