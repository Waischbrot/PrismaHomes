package net.prismaforge.prismahomes.ui;

import net.prismaforge.prismahomes.storage.DataHome;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.CompletableFuture;

public final class EditHomeMenu extends PrismaInventory {
    private final PrismaLanguage language;
    private final SurvivalPlayer data;
    private final DataHome home;

    public EditHomeMenu(final Player player, final SurvivalPlayer data, final DataHome home, final PrismaLanguage language) {
        super(player, "edit_homes", LangKey.MENU_EDITHOME_TITLE.translate(language), 3);
        this.data = data;
        this.language = language;
        this.home = home;
    }

    @Override
    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {
        new PrismaSound(Sound.ITEM_TRIDENT_RETURN, 2, 0.2f).playToIndividual(player);
        CompletableFuture.runAsync(() -> {
            fillGui(SimpleMenuItem.BLACK_PANE.get());

            //icon with home information
            final ItemBuilder builder = new ItemBuilder(Material.valueOf(home.material()));
            builder.name(LangKey.MENU_ITEM_EDITICON_TITLE.translate(language, s ->
                    s.replaceAll("%name%", home.displayName())));
            builder.stringLores(LangKey.MENU_ITEM_EDITICON_LORE.translateList(language, s ->
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
            addButton(4, builder.build());

            //go back
            addButton(22, new Button(new ItemBuilder(Material.BARRIER).name(LangKey.MENU_ITEM_BACK_TITLE.translate(language)).build())
                    .setClickEventConsumer(e -> new ListHomesMenu(player, data, language, home.key()).open()));

            //set location here
            addButton(10, new Button(new ItemBuilder(Material.ENDER_PEARL).name(LangKey.MENU_ITEM_SETHERE_TITLE.translate(language))
                    .stringLores(LangKey.MENU_ITEM_SETHERE_LORE.translateList(language)).build())
                    .setClickEventConsumer(e -> {
                        final Location location = player.getLocation();
                        this.home.world(location.getWorld().getName())
                                .x(location.x()).y(location.y()).z(location.z())
                                .yaw(location.getYaw()).pitch(location.getPitch());
                        new ListHomesMenu(player, data, language, home.key()).open();
                        new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).playToIndividual(player);
                        MessageUtil.message(player, LangKey.HOMES_CHANGED_LOCATION, LangKey.PREFIX_HOMES);
                        saveSecure();
                    }));

            //change displayname
            addButton(12, new Button(new ItemBuilder(Material.NAME_TAG).name(LangKey.MENU_ITEM_RENAME_TITLE.translate(language))
                    .stringLores(LangKey.MENU_ITEM_RENAME_LORE.translateList(language)).build())
                    .setClickEventConsumer(e -> {
                        ChatListener.addTask(player, input -> {
                            if (input.length() <= 40) {
                                home.displayName(input);
                                MessageUtil.message(player, LangKey.HOMES_RENAME_SUCCESS, LangKey.PREFIX_HOMES);
                                new PrismaSound(Sound.ENTITY_PLAYER_LEVELUP, 2, 0.2f).playToIndividual(player);
                                saveSecure();
                            } else {
                                new PrismaSound(Sound.BLOCK_ANVIL_USE, 2, 0.2f).playToIndividual(player);
                                MessageUtil.message(player, LangKey.HOMES_RENAME_INPUT_TOO_LONG, LangKey.PREFIX_HOMES);
                            }
                            PrismaCoreLibrary.syncScheduler().run(() -> new EditHomeMenu(player, data, home, language).open());
                        });
                        MessageUtil.message(player, LangKey.HOMES_ENTER_NEW_NAME, LangKey.PREFIX_HOMES);
                        player.closeInventory();
                    }));

            //change icon
            addButton(14, new Button(new ItemBuilder(Material.WHITE_WOOL).name(LangKey.MENU_ITEM_SETICON_TITLE.translate(language))
                    .stringLores(LangKey.MENU_ITEM_SETICON_LORE.translateList(language)).build())
                    .setClickEventConsumer(e -> new ChangeHomeIconMenu(player, data, home, language).open()));

            //delete home and return to home list
            addButton(16, new Button(new SkullBuilder()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmUwZmQxMDE5OWU4ZTRmY2RhYmNhZTRmODVjODU5MTgxMjdhN2M1NTUzYWQyMzVmMDFjNTZkMThiYjk0NzBkMyJ9fX0=")
                    .name(LangKey.MENU_ITEM_DELHOME_TITLE.translate(language)).stringLores(LangKey.MENU_ITEM_DELHOME_LORE.translateList(language)).build())
                    .setClickEventConsumer(e -> {
                        this.data.homes().remove(home); //remove home from list
                        new ListHomesMenu(player, data, language, home.key()).open();
                        new PrismaSound(Sound.BLOCK_ANVIL_USE, 2, 0.2f).playToIndividual(player);
                        MessageUtil.message(player, LangKey.HOMES_DELETED_HOME, LangKey.PREFIX_HOMES);
                        saveSecure();
                    }));
        });
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
}