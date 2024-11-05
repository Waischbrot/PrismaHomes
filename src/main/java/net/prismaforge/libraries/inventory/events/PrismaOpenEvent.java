package net.prismaforge.libraries.inventory.events;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.prismaforge.libraries.inventory.basic.PrismaInventory;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryOpenEvent;

@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PrismaOpenEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @NonNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    InventoryOpenEvent event;
    PrismaInventory rubyInventory;
    @NonFinal boolean cancelled;

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}