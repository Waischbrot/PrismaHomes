package net.prismaforge.libraries.strings;

import lombok.NonNull;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class Text {
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private Text() {
        throw new AssertionError();
    }

    @NonNull
    public static Component text(final String message) {
        return MINI_MESSAGE.deserialize(message).font(fontKey("minecraft:default"));
    }

    @NonNull
    public static Component convertImage(final String message, final @KeyPattern String font) {
        return MINI_MESSAGE.deserialize("<white>" + message + "</white>").font(fontKey(font));
    }

    @NonNull
    public static String image(final String emoji, final @KeyPattern String font) {
        return "<white><font:" + font + ">" + emoji + "</font>";
    }

    @NonNull
    public static Component empty() {
        return Component.empty();
    }

    @NonNull
    public static Key fontKey(final @KeyPattern String font) {
        return Key.key(font);
    }
}
