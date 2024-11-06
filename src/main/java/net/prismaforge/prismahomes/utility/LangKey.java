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

    PREFIX("survival.core.prefix.homes", "<gradient:#ff960a:yellow><bold>Homes</bold></gradient> <dark_gray>• <gray>"),

    MENU_ITEM_CLOSE_TITLE("survival.menu.item.close.title", "<#d00011>✘ Menü schließen"),
    MENU_ITEM_BACK_TITLE("survival.menu.item.back.title", "<#d00011>✘ Zurück"),
    MENU_ITEM_NO_PREVIOUS_PAGE_TITLE("survival.menu.item.no-previous-page.title", "<gray>Vorherige Seite"),
    MENU_ITEM_PREVIOUS_PAGE_TITLE("survival.menu.item.previous-page.title", "<gold>Vorherige Seite"),
    MENU_ITEM_NO_NEXT_PAGE_TITLE("survival.menu.item.no-next-page.title", "<gray>Nächste Seite"),
    MENU_ITEM_NEXT_PAGE_TITLE("survival.menu.item.next-page.title", "<gold>Nächste Seite"),
    MENU_ITEM_GENERICHOME_TITLE("survival.menu.item.generic-home.title", "%name% <reset><dark_gray>(<yellow>Ort<dark_gray>)"),
    MENU_ITEM_GENERICHOME_LORE("survival.menu.item.generic-home.lore", "<dark_gray>Schlüssel: %key%\n\n<gray>Welt<dark_gray>: %world%\n<gray>X<dark_gray>: <yellow>%x%<gray>, Y<dark_gray>: <yellow>%y%<gray>, Z<dark_gray>: <yellow>%z%\n\n<green>Linksklick um zu teleportieren!\n<dark_gray>Rechtsklick zum Verwalten"),
    MENU_ITEM_EDITICON_TITLE("survival.menu.item.edit-home-icon.title", "%name% <dark_gray>(<yellow>Ort<dark_gray>)"),
    MENU_ITEM_EDITICON_LORE("survival.menu.item.edit-home-icon.lore", "<dark_gray>Schlüssel: %key%\n\n<gray>Welt<dark_gray>: %world%\n<gray>X<dark_gray>: <yellow>%x%<gray>, Y<dark_gray>: <yellow>%y%<gray>, Z<dark_gray>: <yellow>%z%\n\n<dark_gray>Dieses Home wird bearbeitet."),
    MENU_ITEM_HOMEICON_TITLE("survival.menu.item.home-icon.title", "<gold><bold>Home Übersicht"),
    MENU_ITEM_HOMEICON_LORE("survival.menu.item.home-icon.lore", "<gray>Klicke auf ein Home, um dich entweder\n<gray>zu teleportieren, oder es zu verwalten!\n\n<gold>Slots <dark_gray>» <yellow>%homes% <gray>von <white>%slots%\n<dark_gray>Höherer Rang = Mehr Slots"),
    MENU_ITEM_SETHERE_TITLE("survival.menu.item.set-here.title", "<gold>Teleportpunkt setzen"),
    MENU_ITEM_SETHERE_LORE("survival.menu.item.set-here.lore", "<gray>Setzt den Ort, zu welchem dieses Home\n<gray>teleportiert, an <yellow>deine derzeitige Position<gray>!\n\n<dark_gray>Klicke um zu setzen"),
    MENU_ITEM_RENAME_TITLE("survival.menu.item.rename.title", "<gold>Menünamen ändern"),
    MENU_ITEM_RENAME_LORE("survival.menu.item.rename.lore", "<gray>Ändert den Anzeigenamen dieses Homes\n<gray>im Menü auf die Eingabe.\n\n<yellow>Vorsicht, dies ändert nicht\n<yellow>den Eingabeschlüssel!\n\n<dark_gray>Klicke für Eingabe"),
    MENU_ITEM_SETICON_TITLE("survival.menu.item.set-icon.title", "<gold>Item ändern"),
    MENU_ITEM_SETICON_LORE("survival.menu.item.set-icon.lore", "<gray>Ändere das Item, mit welchem\n<gray>dieses Home angezeigt wird!\n\n<dark_gray>Klicke zum Ändern"),
    MENU_ITEM_DELHOME_TITLE("survival.menu.item.delhome.title", "<red>Home löschen"),
    MENU_ITEM_DELHOME_LORE("survival.menu.item.delhome.lore", "<gray>Löscht dieses Home aus der Liste!\n\n<yellow>Diese Aktion kann nicht\n<yellow>rückgängig gemacht werden!\n\n<dark_gray>Klicke zum Löschen"),
    MENU_ITEM_GENERIC_MATERIAL_TITLE("survival.menu.item.generic-material.title", "<green>Materialauswahl"),
    MENU_ITEM_GENERIC_MATERIAL_LORE("survival.menu.item.generic-material.lore", "<dark_gray>Klicke um Auszuwählen"),
    MENU_HOMES_TITLE("survival.menu.homes.title", "<dark_gray>Deine Home-Punkte"),
    MENU_EDITHOME_TITLE("survival.menu.edithome.title", "<dark_gray>Home bearbeiten"),
    MENU_CHANGEICON_TITLE("survival.menu.changeicon.title", "<dark_gray>Item-Auswahl"),
    HOMES_NO_HOME_FOUND("survival.core.homes.error.no-home-found", "<red>Du scheinst kein Home mit diesem Namen zu haben!"),
    HOMES_NO_SLOTS("survival.core.homes.error.no-slots", "<red>Du kannst leider keine weiteren Homes mehr erstellen, da du keine <yellow>freien Slots <red>mehr hast!"),
    HOMES_NEW_HOME_CREATED("survival.core.homes.new-home-created", "<gray>Du hast <green>erfolgreich <gray>ein Home mit dem Namen <yellow>%name% <gray>erstellt!"),
    HOMES_HOME_ALREADY_PRESENT("survival.core.homes.error.already-present", "<red>Du hast bereits ein Home mit diesem Schlüssel."),
    HOMES_INVALID_NAME("survival.core.homes.error.invalid-name", "<red>Dein Name ist zu lang oder enthält unerlaubte Sonderzeichen!"),
    HOMES_CHANGED_MATERIAL("survival.core.homes.changed-material", "<gray>Du hast das <yellow>Item <gray>des Homes <green>erfolgreich <gray>geändert!"),
    HOMES_CHANGED_LOCATION("survival.core.homes.changed-location", "<gray>Du hast den <yellow>Teleportpunkt <gray>des Homes geändert!"),
    HOMES_DELETED_HOME("survival.core.homes.deleted-home", "<gray>Dieses Home wurde permanent <red>gelöscht<gray>. Du kannst es nicht wieder herstellen!"),
    HOMES_RENAME_INPUT_TOO_LONG("survival.core.homes.input-too-long", "<gray>Dein neuer Name darf <yellow>maximal 40 Zeichen <gray>haben."),
    HOMES_ENTER_NEW_NAME("survival.core.homes.enter-new-name", "<gray>Gib den neuen Namen in den Chat ein. Du kannst dabei auch <rainbow>Farbcodes</rainbow> <gray>nutzen!"),
    HOMES_RENAME_SUCCESS("survival.core.homes.rename-success", "<gray>Du hast dieses Home <green>erfolgreich <gray>umbenannt!"),

    COMMAND_NOT_AVAILABLE("survival.core.command-not-available", "<red>Dieser Befehl existiert nicht oder ist nicht für dich verfügbar!");

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
