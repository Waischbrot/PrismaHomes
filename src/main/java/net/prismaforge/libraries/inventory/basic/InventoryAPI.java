package net.prismaforge.libraries.inventory.basic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class InventoryAPI {
    @Getter private static InventoryAPI INSTANCE;
    HashMap<UUID, PrismaInventory> players;
    JavaPlugin plugin;

    private InventoryAPI(final JavaPlugin plugin) {
        this.players = new HashMap<>();
        this.plugin = plugin;
    }

    public static void init(final JavaPlugin plugin) {
        INSTANCE = new InventoryAPI(plugin);
        final Listener listener = new PrismaInventoryListener(INSTANCE);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Nullable
    public PrismaInventory getPlayersInv(final Player player) {
        if (player == null) return null;
        return this.players.get(player.getUniqueId());
    }

    @Nullable
    public PrismaInventory getInvFromInv(final Inventory inventory) {
        for (final PrismaInventory prismaInventory : players.values()) {
            if (prismaInventory.getInventory().equals(inventory)) {
                return prismaInventory;
            }
        }
        return null;
    }
}
