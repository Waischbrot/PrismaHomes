package net.prismaforge.libraries.inventory.basic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true, fluent = true)
@Getter @Setter
public class Button {
    final ItemStack item;
    Consumer<InventoryClickEvent> clickAction;
    Consumer<InventoryDragEvent> dragAction;

    public Button(final ItemStack item) {
        this.item = item;
        this.dragAction = event -> {};
        this.clickAction = event -> {};
    }

    public Button(final Material material) {
        this(new ItemStack(material));
    }
}
