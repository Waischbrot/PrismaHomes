package net.prismaforge.libraries.inventory.basic;

import net.prismaforge.libraries.inventory.events.PrismaClickEvent;
import net.prismaforge.libraries.inventory.events.PrismaCloseEvent;
import net.prismaforge.libraries.inventory.events.PrismaDragEvent;
import net.prismaforge.libraries.inventory.events.PrismaOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class PrismaInventoryListener implements Listener {
    private final InventoryAPI inventoryAPI;

    public PrismaInventoryListener(final InventoryAPI inventoryAPI) {
        this.inventoryAPI = inventoryAPI;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player)) return;

        final PrismaInventory openGui = inventoryAPI.getPlayersInv(player);
        if (openGui == null) return;
        if (event(new PrismaClickEvent(event, openGui))) return;

        final boolean shouldNotProtect = openGui.handleInventoryClickEvent(event);
        final int i = event.getRawSlot();

        if (!shouldNotProtect) {
            if (event.getSlot() == i) {
                event.setCancelled(true);
            } else {
                switch (event.getAction()) {
                    case MOVE_TO_OTHER_INVENTORY, COLLECT_TO_CURSOR, UNKNOWN -> event.setCancelled(true);
                }
            }
        } else {
            event.setCancelled(false);
        }

        final Button button = openGui.getItems().get(i);
        if (button == null) return;
        button.clickAction().accept(event);
    }

    @EventHandler
    public void onDrag(final InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player)) return;

        final PrismaInventory openGui = inventoryAPI.getPlayersInv(player);
        if (openGui == null) return;
        if (event(new PrismaDragEvent(event, openGui))) return;

        event.setCancelled(!openGui.handleInventoryDragEvent(event));
        for (int i : event.getRawSlots()) {
            final Button button = openGui.getItems().get(i);
            if (button == null) return;
            button.dragAction().accept(event);
        }

    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof final Player player)) return;

        final PrismaInventory openGui = inventoryAPI.getPlayersInv(player);
        if (openGui == null) return;
        if (!event.getInventory().equals(openGui.getInventory())) return;
        if (event(new PrismaCloseEvent(event, openGui))) return;

        openGui.handleInventoryCloseEvent(event);
        openGui.setClosed(true);
        inventoryAPI.getPlayers().remove(player.getUniqueId());

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpen(final InventoryOpenEvent event) {

        if (!(event.getPlayer() instanceof final Player player)) return;

        final PrismaInventory openGui = inventoryAPI.getPlayersInv(player);
        if (openGui == null) return;
        if (event(new PrismaOpenEvent(event, openGui))) return;
        if (event.isCancelled()) return;

        openGui.handleInventoryOpenEvent(event);
    }

    private static boolean event(Event event) {
        Bukkit.getPluginManager().callEvent(event);
        return event instanceof Cancellable && ((Cancellable) event).isCancelled();
    }
}
