package net.prismaforge.prismahomes.utility;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@Accessors(fluent = true)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LangKey {
    PREFIX("messages.prefix.homes", "&6&lHomes &8• &7"),
    MENU_CLOSE_TITLE("messages.menu.close.title", "#d00011✘ Menü schließen"),
    MENU_BACK_TITLE("messages.menu.back.title", "#d00011✘ Zurück"),
    MENU_NO_PREV_PAGE("messages.menu.page.previous.none", "&7Vorherige Seite"),
    MENU_PREV_PAGE("messages.menu.page.previous.available", "&6Vorherige Seite"),
    MENU_NO_NEXT_PAGE("messages.menu.page.next.none", "&7Nächste Seite"),
    MENU_NEXT_PAGE("messages.menu.page.next.available", "&6Nächste Seite"),
    MENU_HOME_TITLE("messages.menu.home.title", "%name% &8(&eOrt&8)"),
    MENU_HOME_LORE("messages.menu.home.lore", "&8Schlüssel: %key%\n\n&7Welt&8: %world%\n&7X&8: &e%x%&7, Y&8: &e%y%&7, Z&8: &e%z%\n\n&aLinksklick um zu teleportieren!\n&8Rechtsklick zum Verwalten"),
    MENU_EDIT_ICON_TITLE("messages.menu.edit.icon.title", "%name% &8(&eOrt&8)"),
    MENU_EDIT_ICON_LORE("messages.menu.edit.icon.lore", "&8Schlüssel: %key%\n\n&7Welt&8: %world%\n&7X&8: &e%x%&7, Y&8: &e%y%&7, Z&8: &e%z%\n\n&8Dieses Home wird bearbeitet."),
    MENU_OVERVIEW_TITLE("messages.menu.overview.title", "&6&lHome Übersicht"),
    MENU_OVERVIEW_LORE("messages.menu.overview.lore", "&7Klicke auf ein Home, um dich entweder\n&7zu teleportieren, oder es zu verwalten!\n\n&6Slots &8» &e%homes% &7von &f%slots%\n&8Höherer Rang = Mehr Slots"),
    MENU_SETPOINT_TITLE("messages.menu.setpoint.title", "&6Teleportpunkt setzen"),
    MENU_SETPOINT_LORE("messages.menu.setpoint.lore", "&7Setzt den Ort, zu welchem dieses Home\n&7teleportiert, an &edeine derzeitige Position&7!\n\n&8Klicke um zu setzen"),
    MENU_RENAME_TITLE("messages.menu.rename.title", "&6Menünamen ändern"),
    MENU_RENAME_LORE("messages.menu.rename.lore", "&7Ändert den Anzeigenamen dieses Homes\n&7im Menü auf die Eingabe.\n\n&eVorsicht, dies ändert nicht\n&eden Eingabeschlüssel!\n\n&8Klicke für Eingabe"),
    MENU_CHANGE_ITEM_TITLE("messages.menu.change.item.title", "&6Item ändern"),
    MENU_CHANGE_ITEM_LORE("messages.menu.change.item.lore", "&7Ändere das Item, mit welchem\n&7dieses Home angezeigt wird!\n\n&8Klicke zum Ändern"),
    MENU_DELETE_TITLE("messages.menu.delete.title", "&cHome löschen"),
    MENU_DELETE_LORE("messages.menu.delete.lore", "&7Löscht dieses Home aus der Liste!\n\n&eDiese Aktion kann nicht\n&erückgängig gemacht werden!\n\n&8Klicke zum Löschen"),
    MENU_MATERIAL_TITLE("messages.menu.material.title", "&aMaterialauswahl"),
    MENU_MATERIAL_LORE("messages.menu.material.lore", "&8Klicke um Auszuwählen"),
    MENU_MAIN_TITLE("messages.menu.main.title", "&8Deine Home-Punkte"),
    MENU_EDIT_TITLE("messages.menu.edit.title", "&8Home bearbeiten"),
    MENU_CHANGE_ICON_TITLE("messages.menu.icon.change.title", "&8Item-Auswahl"),
    ERROR_NO_HOME("messages.error.no_home", "&cDu scheinst kein Home mit diesem Namen zu haben!"),
    ERROR_NO_SLOTS("messages.error.no_slots", "&cDu kannst leider keine weiteren Homes mehr erstellen, da du keine &efreien Slots &cmehr hast!"),
    SUCCESS_NEW_HOME("messages.success.new_home", "&7Du hast &aerfolgreich &7ein Home mit dem Namen &e%name% &7erstellt!"),
    ERROR_DUPLICATE_HOME("messages.error.duplicate_home", "&cDu hast bereits ein Home mit diesem Schlüssel."),
    ERROR_INVALID_NAME("messages.error.invalid_name", "&cDein Name ist zu lang oder enthält unerlaubte Sonderzeichen!"),
    SUCCESS_MATERIAL_CHANGED("messages.success.material_changed", "&7Du hast das &eItem &7des Homes &aerfolgreich &7geändert!"),
    SUCCESS_LOCATION_CHANGED("messages.success.location_changed", "&7Du hast den &eTeleportpunkt &7des Homes geändert!"),
    SUCCESS_HOME_DELETED("messages.success.home_deleted", "&7Dieses Home wurde permanent &cgelöscht&7. Du kannst es nicht wieder herstellen!"),
    ERROR_NAME_TOO_LONG("messages.error.name_too_long", "&7Dein neuer Name darf &emaximal 40 Zeichen &7haben."),
    PROMPT_NEW_NAME("messages.prompt.new_name", "&7Gib den neuen Namen in den Chat ein. Du kannst dabei auch &cFarbcodes &7nutzen!"),
    SUCCESS_RENAME("messages.success.rename", "&7Du hast dieses Home &aerfolgreich &7umbenannt!");

    private final String key;
    private final String defaultValue;

    public String translate(final Config config) {
        return config.getConfigField(key, defaultValue);
    }

    public String translate(final Config config, final Function<String, String> modifier) {
        String result = translate(config);
        result = modifier.apply(result);
        return result;
    }

    @NonNull
    public List<String> translateList(final Config config) {
        return translateList(config, s -> s);
    }

    @NonNull
    public List<String> translateList(final Config config, final Function<String, String> modifier) {
        String result = translate(config); //translation
        result = modifier.apply(result); //transform string
        final List<String> transformed = new ArrayList<>(); //create empty list
        Collections.addAll(transformed, result.split("\n")); //split into different lines
        return transformed;
    }
}
