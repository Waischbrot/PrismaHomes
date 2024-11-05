package net.prismaforge.libraries.inventory.events;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

public class FakeCloseEvent extends InventoryCloseEvent {

    public FakeCloseEvent(final InventoryView transaction) {
        super(transaction);
    }
}
