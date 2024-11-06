package net.prismaforge.prismahomes.ui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.inventory.SimpleMenuItem;
import net.prismaforge.libraries.inventory.basic.Button;
import net.prismaforge.libraries.inventory.basic.PrismaInventory;
import net.prismaforge.libraries.inventory.paged.Pagination;
import net.prismaforge.libraries.wrapper.PrismaSound;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.storage.DataHome;
import net.prismaforge.prismahomes.storage.DataPlayer;
import net.prismaforge.prismahomes.utility.SlotsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ListHomesMenu extends PrismaInventory {
    final DataPlayer data;
    final PrismaHomes plugin;
    final Pagination pagination;
    final int slots;

    public ListHomesMenu(final Player player, final PrismaHomes plugin, final DataPlayer data) {
        super(player, "list_homes", plugin.getConfiguration().getConfigField("menu.list-homes.title"), 5);
        this.data = data;
        this.plugin = plugin;
        this.slots = SlotsUtil.availableSlots(player); //calculate how many home slots player has max!
        this.pagination = new Pagination(this);
        this.pagination.registerPageSlotsBetween(10, 16);
        this.pagination.registerPageSlotsBetween(19, 25);
        this.pagination.registerPageSlotsBetween(28, 34);
        registerPagination();
    }

    private void registerPagination() {
        for (final DataHome home : data.homes()) {
            final ItemBuilder builder = new ItemBuilder(Material.valueOf(home.material()));
            builder.name(LangKey.MENU_ITEM_GENERICHOME_TITLE.translate(language, s ->
                    s.replaceAll("%name%", home.displayName())));
            builder.stringLores(LangKey.MENU_ITEM_GENERICHOME_LORE.translateList(language, s ->
                    s.replaceAll("%world%", HomeUtil.prettyName(home.world(), language))
                            .replaceAll("%key%", home.key())
                            .replaceAll("%x%", ValueFormatter.formatTwoDecimals(home.x()))
                            .replaceAll("%y%", ValueFormatter.formatTwoDecimals(home.y()))
                            .replaceAll("%z%", ValueFormatter.formatTwoDecimals(home.z()))
            ));

            //add all itemflags to prevent weird displays!
            for (final ItemFlag itemFlag : ItemFlag.values()) {
                builder.addItemFlags(itemFlag);
            }

            //add button to pagination
            this.pagination.addButton(new Button(builder.build())
                    .setClickEventConsumer(e -> {
                        if (e.isLeftClick()) {
                            TeleportUtil.teleport(player, new Location(Bukkit.getWorld(home.world()), home.x(), home.y(), home.z(), home.yaw(), home.pitch()));
                            player.closeInventory();
                        } else if (e.isRightClick()) {
                            new EditHomeMenu(player, data, home, language).open();
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
            addButton(4, new SkullBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Y3Y2RlZWZjNmQzN2ZlY2FiNjc2YzU4NGJmNjIwODMyYWFhYzg1Mzc1ZTlmY2JmZjI3MzcyNDkyZDY5ZiJ9fX0=")
                    .name(LangKey.MENU_ITEM_HOMEICON_TITLE.translate(language))
                    .stringLores(LangKey.MENU_ITEM_HOMEICON_LORE.translateList(language, s ->
                            s.replaceAll("%homes%", String.valueOf(data.homes().size())).replaceAll("%slots%", String.valueOf(this.slots))))
                    .build());

            //close inventory!
            addButton(40, new Button(new ItemBuilder(Material.BARRIER).name(LangKey.MENU_ITEM_CLOSE_TITLE.translate(language)).build())
                    .setClickEventConsumer(e -> player.closeInventory()));

            this.pagination.update(); //place all icons into inv!
            placePageButtons(); //switch side buttons
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
