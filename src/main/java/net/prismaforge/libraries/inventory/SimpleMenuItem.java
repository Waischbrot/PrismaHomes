package net.prismaforge.libraries.inventory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.items.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Accessors(fluent = true)
@Getter
public enum SimpleMenuItem {
    BUTTON(STONE_BUTTON),
    WHITE(WHITE_STAINED_GLASS_PANE),
    LIGHT_GRAY(LIGHT_GRAY_STAINED_GLASS_PANE),
    GRAY(GRAY_STAINED_GLASS_PANE),
    BLACK(BLACK_STAINED_GLASS_PANE),
    BROWN(BROWN_STAINED_GLASS_PANE),
    RED(RED_STAINED_GLASS_PANE),
    ORANGE(ORANGE_STAINED_GLASS_PANE),
    YELLOW(YELLOW_STAINED_GLASS_PANE),
    LIME(LIME_STAINED_GLASS_PANE),
    GREEN(GREEN_STAINED_GLASS_PANE),
    CYAN(CYAN_STAINED_GLASS_PANE),
    LIGHT_BLUE(LIGHT_BLUE_STAINED_GLASS_PANE),
    BLUE(BLUE_STAINED_GLASS_PANE),
    PURPLE(PURPLE_STAINED_GLASS_PANE),
    MAGENTA(MAGENTA_STAINED_GLASS_PANE),
    PINK(PINK_STAINED_GLASS_PANE);

    ItemStack item;

    SimpleMenuItem(final Material material) {
        this.item = Items.createItem(material, itemCreator -> itemCreator.name("&7"));
    }
}
