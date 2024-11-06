package net.prismaforge.prismahomes.ui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.libraries.inventory.SimpleMenuItem;
import net.prismaforge.libraries.inventory.basic.Button;
import net.prismaforge.libraries.inventory.basic.PrismaInventory;
import net.prismaforge.libraries.inventory.paged.Pagination;
import net.prismaforge.libraries.items.Items;
import net.prismaforge.libraries.strings.NumberFormat;
import net.prismaforge.libraries.wrapper.PrismaSound;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.storage.DataHome;
import net.prismaforge.prismahomes.storage.DataPlayer;
import net.prismaforge.prismahomes.utility.LangKey;
import net.prismaforge.prismahomes.utility.SlotsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ListHomesMenu extends PrismaInventory {
    final DataPlayer data;
    final PrismaHomes plugin;
    final Config config;
    final Pagination pagination;
    final int slots;

    public ListHomesMenu(final Player player, final PrismaHomes plugin, final DataPlayer data) {
        super(player, "list_homes", LangKey.MENU_HOMES_TITLE.translate(plugin.getConfiguration()), 5);
        this.data = data;
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.slots = SlotsUtil.availableSlots(player); //calculate how many home slots player has max!
        this.pagination = new Pagination(this);
        this.pagination.registerPageSlotsBetween(10, 16);
        this.pagination.registerPageSlotsBetween(19, 25);
        this.pagination.registerPageSlotsBetween(28, 34);
        registerPagination();
    }

    private void registerPagination() {
        for (final DataHome home : data.homes()) {
            final ItemStack item = Items.createItem(Material.getMaterial(home.material()), ic -> {
                ic.name(LangKey.MENU_ITEM_GENERICHOME_TITLE.translate(config, s -> s.replaceAll("%name%", home.displayName())));
                ic.lore(LangKey.MENU_ITEM_GENERICHOME_LORE.translateList(config, s ->
                        s.replaceAll("%world%", home.world())
                                .replaceAll("%key%", home.key())
                                .replaceAll("%x%", NumberFormat.twoDecimals(home.x()))
                                .replaceAll("%y%", NumberFormat.twoDecimals(home.y()))
                                .replaceAll("%z%", NumberFormat.twoDecimals(home.z()))
                ));
                ic.flag(ItemFlag.values());
            });
            this.pagination.addButton(new Button(item)
                    .clickAction(e -> {
                        if (e.isLeftClick()) {
                            player.teleport(new Location(Bukkit.getWorld(home.world()), home.x(), home.y(), home.z(), home.yaw(), home.pitch()));
                            player.closeInventory();
                        } else if (e.isRightClick()) {
                            new EditHomeMenu(player, plugin, data, home).open();
                        }
                    }));
        }
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        new PrismaSound(Sound.ITEM_TRIDENT_RETURN, 2, 0.2f).play(player);
        CompletableFuture.runAsync(() -> {
            fillGui(SimpleMenuItem.BLACK.item());

            //place menu icon!
            addButton(4, Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y3Y2RlZWZjNmQzN2ZlY2FiNjc2YzU4NGJmNjIwODMyYWFhYzg1Mzc1ZTlmY2JmZjI3MzcyNDkyZDY5ZiJ9fX0=");
                sc.name(LangKey.MENU_ITEM_HOMEICON_TITLE.translate(config));
                sc.lore(LangKey.MENU_ITEM_HOMEICON_LORE.translateList(config, s ->
                        s.replaceAll("%homes%", String.valueOf(data.homes().size())).replaceAll("%slots%", String.valueOf(this.slots))));
            }));

            //close inventory!
            addButton(40, new Button(Items.createItem(Material.BARRIER, ic -> {
                ic.name(LangKey.MENU_ITEM_CLOSE_TITLE.translate(config));
            })).clickAction(e -> player.closeInventory()));

            this.pagination.update(); //place all icons into inv!
            placePageButtons(); //switch side buttons
        });
    }

    private void placePageButtons() {
        if (!pagination.isFirstPage()) {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
                sc.name(LangKey.MENU_ITEM_PREVIOUS_PAGE_TITLE.translate(config));
            });
            addButton(38, new Button(skull)
                    .clickAction(e -> {
                        pagination.previousPage().update();
                        placePageButtons();
                    }));
        } else {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=");
                sc.name(LangKey.MENU_ITEM_NO_PREVIOUS_PAGE_TITLE.translate(config));
            });
            addButton(38, new Button(skull));
        }

        if (!pagination.isLastPage()) {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");
                sc.name(LangKey.MENU_ITEM_NEXT_PAGE_TITLE.translate(config));
            });
            addButton(42, new Button(skull)
                    .clickAction(e -> {
                        pagination.nextPage().update();
                        placePageButtons();
                    }));
        } else {
            final ItemStack skull = Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFhMTg3ZmVkZTg4ZGUwMDJjYmQ5MzA1NzVlYjdiYTQ4ZDNiMWEwNmQ5NjFiZGM1MzU4MDA3NTBhZjc2NDkyNiJ9fX0=");
                sc.name(LangKey.MENU_ITEM_NO_NEXT_PAGE_TITLE.translate(config));
            });
            addButton(42, new Button(skull));
        }
    }
}
