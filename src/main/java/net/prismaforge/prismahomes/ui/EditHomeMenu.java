package net.prismaforge.prismahomes.ui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
    DataPlayer data;
    DataHome home;

    public EditHomeMenu(Player player, PrismaHomes plugin, DataPlayer data, DataHome home) {
        super(player, "edit_homes", LangKey.MENU_EDIT_TITLE.translate(), 3);
        this.plugin = plugin;
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
                ic.name(LangKey.MENU_EDIT_ICON_TITLE.translate(s -> s.replaceAll("%name%", home.displayName())));
                ic.lore(LangKey.MENU_EDIT_ICON_LORE.translateList(s ->
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
                ic.name(LangKey.MENU_BACK_TITLE.translate());
            })).clickAction(e -> new ListHomesMenu(player, plugin, data).open()));

            //set location here
            addButton(10, new Button(Items.createItem(Material.ENDER_PEARL, ic -> {
                ic.name(LangKey.MENU_SETPOINT_TITLE.translate());
                ic.lore(LangKey.MENU_SETPOINT_LORE.translateList());
            })).clickAction(e -> {
                final Location location = player.getLocation();
                this.home.world(location.getWorld().getName())
                        .x(location.getX()).y(location.getY()).z(location.getZ())
                        .yaw(location.getYaw()).pitch(location.getPitch());
                new ListHomesMenu(player, plugin, data).open();
                new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).play(player);
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.SUCCESS_LOCATION_CHANGED.translate()));
                saveSecure();
            }));

            //change displayname
            addButton(12, new Button(Items.createItem(Material.NAME_TAG, ic -> {
                ic.name(LangKey.MENU_RENAME_TITLE.translate());
                ic.lore(LangKey.MENU_RENAME_LORE.translateList());
            })).clickAction(e -> {
                ChatListener.addTask(player, input -> {
                    if (input.length() <= 40) {
                        home.displayName(input);
                        player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.SUCCESS_RENAME.translate()));
                        new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).play(player);
                        saveSecure();
                    } else {
                        new PrismaSound(Sound.BLOCK_ANVIL_USE, 2, 0.2f).play(player);
                        player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.ERROR_NAME_TOO_LONG.translate()));
                    }
                    new Scheduler(plugin, false).run(() -> new EditHomeMenu(player, plugin, data, home).open());
                });
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.PROMPT_NEW_NAME.translate()));
                player.closeInventory();
            }));

            //change icon
            addButton(14, new Button(Items.createItem(Material.WHITE_WOOL, ic -> {
                ic.name(LangKey.MENU_CHANGE_ITEM_TITLE.translate());
                ic.lore(LangKey.MENU_CHANGE_ITEM_LORE.translateList());
            })).clickAction(e -> new ChangeIconMenu(player, plugin, data, home).open()));

            //delete home and return to home list
            addButton(16, new Button(Items.createSkull(sc -> {
                sc.byTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmUwZmQxMDE5OWU4ZTRmY2RhYmNhZTRmODVjODU5MTgxMjdhN2M1NTUzYWQyMzVmMDFjNTZkMThiYjk0NzBkMyJ9fX0=");
                sc.name(LangKey.MENU_DELETE_TITLE.translate());
                sc.lore(LangKey.MENU_DELETE_LORE.translateList());
            })).clickAction(e -> {
                this.data.homes().remove(home); //remove home from list
                new ListHomesMenu(player, plugin, data).open();
                new PrismaSound(Sound.BLOCK_ANVIL_USE, 2, 0.2f).play(player);
                player.sendMessage(ColorUtil.colorString(LangKey.PREFIX.translate() + LangKey.SUCCESS_HOME_DELETED.translate()));
                saveSecure();
            }));
        });
    }

    private void saveSecure() {
        PrismaHomes.STORAGE().save(this.data);
    }
}