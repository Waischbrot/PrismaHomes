package net.prismaforge.prismahomes.ui;

import de.prismamc.corelibrary.inventory.SimpleMenuItem;
import de.prismamc.corelibrary.inventory.basic.Button;
import de.prismamc.corelibrary.inventory.basic.PrismaInventory;
import de.prismamc.corelibrary.inventory.pagemenu.PageHandler;
import de.prismamc.corelibrary.items.ItemBuilder;
import de.prismamc.corelibrary.items.SkullBuilder;
import de.prismamc.corelibrary.wrapper.messaging.PrismaSound;
import de.prismamc.datamanagement.PrismaAPI;
import de.prismamc.datamanagement.backendplayer.PrismaLanguage;
import de.prismamc.datamanagement.survival.player.DataHome;
import de.prismamc.datamanagement.survival.player.SurvivalPlayer;
import de.prismamc.survivalcore.language.LangKey;
import de.prismamc.survivalcore.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    private final PrismaLanguage language;
    private final SurvivalPlayer data;
    private final DataHome home;
    private final PageHandler pagination;

    public ChangeIconMenu(final Player player, final SurvivalPlayer data, final DataHome home, final PrismaLanguage language) {
        super(player, "change_icon_homes", LangKey.MENU_CHANGEICON_TITLE.translate(language), 5);
        this.language = language;
        this.data = data;
        this.home = home;
        this.pagination = new PageHandler(this);
        this.pagination.registerPageSlotsBetween(10, 16);
        this.pagination.registerPageSlotsBetween(19, 25);
        this.pagination.registerPageSlotsBetween(28, 34);
    }

    private void registerPagination() {
        for (final Material material : MATERIALS) {
            final ItemBuilder builder = new ItemBuilder(material);
            builder.name(LangKey.MENU_ITEM_GENERIC_MATERIAL_TITLE.translate(language));
            builder.stringLores(LangKey.MENU_ITEM_GENERIC_MATERIAL_LORE.translateList(language));
            //add all itemflags to prevent weird displays!
            for (final ItemFlag itemFlag : ItemFlag.values()) {
                builder.addItemFlags(itemFlag);
            }
            this.pagination.addButton(new Button(builder.build())
                    .setClickEventConsumer(e -> equipMaterial(material)));
        }
    }

    private void equipMaterial(final Material material) {
        this.home.material(material.toString());
        new EditHomeMenu(player, data, home, language).open();
        new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).playToIndividual(player);
        MessageUtil.message(player, LangKey.HOMES_CHANGED_MATERIAL, LangKey.PREFIX_HOMES);
        saveSecure();
    }

    //has to be done this way -> else we would overwrite other modifications done to data meanwhile!
    private void saveSecure() {
        final var temp = PrismaAPI.getSurvivalHandler().fetchHazel(player.getUniqueId());
        if (temp.isPresent()) {
            final SurvivalPlayer newPlayer = temp.get();
            newPlayer.homes(this.data.homes());
            PrismaAPI.getSurvivalHandler().updateHazel(newPlayer);
        }
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        new PrismaSound(Sound.ITEM_TRIDENT_RETURN, 2, 0.2f).playToIndividual(player);
        CompletableFuture.runAsync(() -> {
            fillGui(SimpleMenuItem.BLACK_PANE.get());

            //go back
            addButton(40, new Button(new ItemBuilder(Material.BARRIER).name(LangKey.MENU_ITEM_BACK_TITLE.translate(language)).build())
                    .setClickEventConsumer(e -> new EditHomeMenu(player, data, home, language).open()));

            registerPagination();
            pagination.update();
            placePageButtons();
        });
    }

    private void placePageButtons() {
        if (!pagination.isFirstPage()) {
            addButton(38, new Button(new SkullBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")
                    .name(LangKey.MENU_ITEM_PREVIOUS_PAGE_TITLE.translate(language))
                    .build())
                    .setClickEventConsumer(e -> {
                        pagination.previousPage().update();
                        placePageButtons();
                    }));
        } else {
            addButton(38, new SkullBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=")
                    .name(LangKey.MENU_ITEM_NO_PREVIOUS_PAGE_TITLE.translate(language))
                    .build());
        }
        if (!pagination.isLastPage()) {
            addButton(42, new Button(new SkullBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19")
                    .name(LangKey.MENU_ITEM_NEXT_PAGE_TITLE.translate(language))
                    .build())
                    .setClickEventConsumer(e -> {
                        pagination.nextPage().update();
                        placePageButtons();
                    }));
        } else {
            addButton(42, new SkullBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFhMTg3ZmVkZTg4ZGUwMDJjYmQ5MzA1NzVlYjdiYTQ4ZDNiMWEwNmQ5NjFiZGM1MzU4MDA3NTBhZjc2NDkyNiJ9fX0=")
                    .name(LangKey.MENU_ITEM_NO_NEXT_PAGE_TITLE.translate(language))
                    .build());
        }
    }
}
