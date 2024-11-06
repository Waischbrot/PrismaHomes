package net.prismaforge.prismahomes.ui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.libraries.inventory.SimpleMenuItem;
import net.prismaforge.libraries.inventory.basic.Button;
import net.prismaforge.libraries.inventory.basic.PrismaInventory;
import net.prismaforge.libraries.items.Items;
import net.prismaforge.libraries.scheduler.Scheduler;
import net.prismaforge.libraries.strings.ColorUtil;
import net.prismaforge.libraries.strings.NumberFormat;
import net.prismaforge.libraries.wrapper.PrismaSound;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.listener.ChatListener;
import net.prismaforge.prismahomes.storage.DataHome;
import net.prismaforge.prismahomes.storage.DataPlayer;
import net.prismaforge.prismahomes.utility.LangKey;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class EditHomeMenu extends PrismaInventory {
    PrismaHomes plugin;
    Config config;
    DataPlayer data;
    DataHome home;

    public EditHomeMenu(Player player, PrismaHomes plugin, DataPlayer data, DataHome home) {
        super(player, "edit_homes", LangKey.MENU_EDITHOME_TITLE.translate(plugin.getConfiguration()), 3);
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.data = data;
        this.home = home;
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        new PrismaSound(Sound.ITEM_TRIDENT_RETURN, 2, 0.2f).play(player);
        CompletableFuture.runAsync(() -> {
            fillGui(SimpleMenuItem.BLACK.item());

            //icon with home information
            addButton(4, Items.createItem(Material.getMaterial(home.material()), ic -> {
                ic.name(LangKey.MENU_ITEM_EDITICON_TITLE.translate(config, s -> s.replaceAll("%name%", home.displayName())));
                ic.lore(LangKey.MENU_ITEM_EDITICON_LORE.translateList(config, s ->
                        s.replaceAll("%world%", home.world())
                                .replaceAll("%key%", home.key())
                                .replaceAll("%x%", NumberFormat.twoDecimals(home.x()))
                                .replaceAll("%y%", NumberFormat.twoDecimals(home.y()))
                                .replaceAll("%z%", NumberFormat.twoDecimals(home.z()))
                ));
                ic.flag(ItemFlag.values());
            }));

            //go back
            addButton(22, new Button(Items.createItem(Material.BARRIER, ic -> {
                ic.name(LangKey.MENU_ITEM_BACK_TITLE.translate(config));
            })).clickAction(e -> new ListHomesMenu(player, plugin, data).open()));

            //set location here
            addButton(10, new Button(Items.createItem(Material.ENDER_PEARL, ic -> {
                ic.name(LangKey.MENU_ITEM_SETHERE_TITLE.translate(config));
                ic.lore(LangKey.MENU_ITEM_SETHERE_LORE.translateList(config));
            })).clickAction(e -> {
                final Location location = player.getLocation();
                this.home.world(location.getWorld().getName())
                        .x(location.getX()).y(location.getY()).z(location.getZ())
                        .yaw(location.getYaw()).pitch(location.getPitch());
                new ListHomesMenu(player, plugin, data).open();
                new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).play(player);
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.HOMES_CHANGED_LOCATION.translate(config)));
                saveSecure();
            }));

            //change displayname
            addButton(12, new Button(Items.createItem(Material.NAME_TAG, ic -> {
                ic.name(LangKey.MENU_ITEM_RENAME_TITLE.translate(config));
                ic.lore(LangKey.MENU_ITEM_RENAME_LORE.translateList(config));
            })).clickAction(e -> {
                ChatListener.addTask(player, input -> {
                    if (input.length() <= 40) {
                        home.displayName(input);
                        player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.HOMES_RENAME_SUCCESS.translate(config)));
                        new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).play(player);
                        saveSecure();
                    } else {
                        new PrismaSound(Sound.BLOCK_ANVIL_USE, 2, 0.2f).play(player);
                        player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.HOMES_RENAME_INPUT_TOO_LONG.translate(config)));
                    }
                    new Scheduler(plugin, false).run(() -> new EditHomeMenu(player, plugin, data, home).open());
                });
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.HOMES_ENTER_NEW_NAME.translate(config)));
                player.closeInventory();
            }));

            //change icon
            addButton(14, new Button(Items.createItem(Material.WHITE_WOOL, ic -> {
                ic.name(LangKey.MENU_ITEM_SETICON_TITLE.translate(config));
                ic.lore(LangKey.MENU_ITEM_SETICON_LORE.translateList(config));
            })).clickAction(e -> new ChangeIconMenu(player, plugin, data, home).open()));

            //delete home and return to home list
            addButton(16, new Button(Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmUwZmQxMDE5OWU4ZTRmY2RhYmNhZTRmODVjODU5MTgxMjdhN2M1NTUzYWQyMzVmMDFjNTZkMThiYjk0NzBkMyJ9fX0=");
                sc.name(LangKey.MENU_ITEM_DELHOME_TITLE.translate(config));
                sc.lore(LangKey.MENU_ITEM_DELHOME_LORE.translateList(config));
            })).clickAction(e -> {
                this.data.homes().remove(home); //remove home from list
                new ListHomesMenu(player, plugin, data).open();
                new PrismaSound(Sound.BLOCK_ANVIL_USE, 2, 0.2f).play(player);
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate(config) + LangKey.HOMES_DELETED_HOME.translate(config)));
                saveSecure();
            }));
        });
    }

    private void saveSecure() {
        this.plugin.getStorageHandler().save(this.data);
    }
}