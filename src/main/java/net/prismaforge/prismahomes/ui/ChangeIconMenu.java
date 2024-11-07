package net.prismaforge.prismahomes.ui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.inventory.SimpleMenuItem;
import net.prismaforge.libraries.inventory.basic.Button;
import net.prismaforge.libraries.inventory.basic.PrismaInventory;
import net.prismaforge.libraries.inventory.paged.Pagination;
import net.prismaforge.libraries.items.Items;
import net.prismaforge.libraries.strings.ColorUtil;
import net.prismaforge.libraries.wrapper.PrismaSound;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.storage.DataHome;
import net.prismaforge.prismahomes.storage.DataPlayer;
import net.prismaforge.prismahomes.utility.LangKey;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class ChangeIconMenu extends PrismaInventory {
    private static final List<Material> MATERIALS;

    static {
        MATERIALS = List.of(
                Material.SPAWNER, Material.COAL, Material.CHARCOAL, Material.RAW_IRON, Material.RAW_COPPER, Material.RAW_GOLD, Material.EMERALD, Material.DIAMOND, Material.LAPIS_LAZULI, Material.ANCIENT_DEBRIS, Material.IRON_INGOT, Material.COPPER_INGOT, Material.GOLD_INGOT, Material.NETHERITE_SCRAP, Material.NETHERITE_INGOT, Material.QUARTZ, Material.AMETHYST_SHARD,
                Material.CAMPFIRE, Material.SOUL_CAMPFIRE, Material.GRASS_BLOCK, Material.BLAZE_ROD, Material.BONE, Material.SNOWBALL, Material.EGG, Material.LEATHER, Material.HONEYCOMB, Material.INK_SAC, Material.GLOW_INK_SAC, Material.FLINT, Material.REDSTONE, Material.CHEST,

                Material.GOLDEN_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_AXE, Material.GOLDEN_HOE,

                Material.CARROT, Material.WHEAT, Material.POTATO, Material.APPLE, Material.MELON, Material.PUMPKIN,
                Material.BREAD, Material.GOLDEN_APPLE, Material.MILK_BUCKET, Material.HONEY_BOTTLE, Material.PUMPKIN_PIE, Material.CHORUS_FRUIT, Material.HONEYCOMB, Material.WRITABLE_BOOK, Material.OAK_BOAT, Material.MINECART, Material.RAIL, Material.MUSIC_DISC_11, Material.ANVIL, Material.CRAFTING_TABLE,

                Material.TNT, Material.TOTEM_OF_UNDYING, Material.CROSSBOW, Material.BOW, Material.END_CRYSTAL, Material.DRAGON_BREATH, Material.SLIME_BALL, Material.ENCHANTING_TABLE, Material.BREWING_STAND, Material.RED_BED, Material.END_PORTAL_FRAME, Material.COOKED_COD, Material.POPPY, Material.WARPED_FUNGUS, Material.BUBBLE_CORAL,

                Material.TURTLE_HELMET, Material.TURTLE_EGG, Material.DIAMOND_BLOCK, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD, Material.TRIDENT, Material.TOTEM_OF_UNDYING
        );
    }

    PrismaHomes plugin;
    DataPlayer data;
    DataHome home;
    Pagination pagination;

    public ChangeIconMenu(Player player, PrismaHomes plugin, DataPlayer data, DataHome home) {
        super(player, "change_icon_homes", LangKey.MENU_CHANGE_ICON_TITLE.translate(), 5);
        this.plugin = plugin;
        this.data = data;
        this.home = home;
        this.pagination = new Pagination(this);
        this.pagination.registerPageSlotsBetween(10, 16);
        this.pagination.registerPageSlotsBetween(19, 25);
        this.pagination.registerPageSlotsBetween(28, 34);
    }

    private void registerPagination() {
        for (final Material material : MATERIALS) {
            this.pagination.addButton(new Button(Items.createItem(material, ic -> {
                ic.name(LangKey.MENU_MATERIAL_TITLE.translate());
                ic.lore(LangKey.MENU_MATERIAL_LORE.translateList());
                ic.flag(ItemFlag.values());
            })).clickAction(e -> equipMaterial(material)));
        }
    }

    private void equipMaterial(final Material material) {
        this.home.material(material.toString());
        new EditHomeMenu(player, plugin, data, home).open();
        new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).play(player);
        player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.SUCCESS_MATERIAL_CHANGED.translate()));
        saveSecure();
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        new PrismaSound(Sound.ITEM_TRIDENT_RETURN, 2, 0.2f).play(player);
        CompletableFuture.runAsync(() -> {
            fillGui(SimpleMenuItem.BLACK.item());

            //go back
            addButton(40, new Button(Items.createItem(Material.BARRIER, ic ->
                    ic.name(LangKey.MENU_BACK_TITLE.translate())))
                    .clickAction(e -> new EditHomeMenu(player, plugin, data, home).open()));

            registerPagination();
            pagination.update();
            placePageButtons();
        });
    }

    private void placePageButtons() {
        if (!pagination.isFirstPage()) {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
                sc.name(LangKey.MENU_PREV_PAGE.translate());
            });
            addButton(38, new Button(skull)
                    .clickAction(e -> {
                        pagination.previousPage().update();
                        placePageButtons();
                    }));
        } else {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=");
                sc.name(LangKey.MENU_NO_PREV_PAGE.translate());
            });
            addButton(38, new Button(skull));
        }

        if (!pagination.isLastPage()) {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");
                sc.name(LangKey.MENU_NEXT_PAGE.translate());
            });
            addButton(42, new Button(skull)
                    .clickAction(e -> {
                        pagination.nextPage().update();
                        placePageButtons();
                    }));
        } else {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFhMTg3ZmVkZTg4ZGUwMDJjYmQ5MzA1NzVlYjdiYTQ4ZDNiMWEwNmQ5NjFiZGM1MzU4MDA3NTBhZjc2NDkyNiJ9fX0=");
                sc.name(LangKey.MENU_NO_NEXT_PAGE.translate());
            });
            addButton(42, new Button(skull));
        }
    }

    private void saveSecure() {
        PrismaHomes.STORAGE().save(this.data);
    }
}
