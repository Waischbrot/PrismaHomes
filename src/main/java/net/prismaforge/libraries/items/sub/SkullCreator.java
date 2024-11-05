package net.prismaforge.libraries.items.sub;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.skin.Skin;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class SkullCreator extends ItemCreator {
    private static final UUID HEAD_UUID = UUID.fromString("06be0255-1acd-4442-91df-68b44f1c61c6");
    private static Method SKULL_METHOD;
    String texture;
    UUID profileUUID;

    /**
     * Create a new SkullCreator
     */
    public SkullCreator() {
        super(Material.PLAYER_HEAD);
    }

    @NonNull
    public SkullCreator byPlayer(final Player player) {
        return byUUID(player.getUniqueId());
    }

    @NonNull
    public SkullCreator byUUID(final UUID uuid) {
        final Skin skin = Skin.fromUUID(uuid);
        this.texture = skin.getTexture();
        return this;
    }

    @NonNull
    public SkullCreator byName(final String name) {
        final Skin skin = Skin.fromName(name);
        this.texture = skin.getTexture();
        return this;
    }

    @NonNull
    public SkullCreator byTexture(final String texture) {
        this.texture = texture;
        return this;
    }

    // only relevant for stacking these items!
    @NonNull
    public SkullCreator metaUUID(final UUID uuid) {
        this.profileUUID = uuid;
        return this;
    }

    /**
     * Builds the ItemStack this creator is specified to create!
     * @return Finished ItemStack
     */
    @NonNull
    public ItemStack build() {
        final ItemStack stack = super.build();
        final ItemMeta meta = stack.getItemMeta();
        if (!(meta instanceof SkullMeta skullMeta)) return stack; //can't modify skull meta
        if (this.texture == null) return stack; //no texture present for this stack

        if (SKULL_METHOD == null) {
            try {
                SKULL_METHOD = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                SKULL_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        final UUID uuid = this.profileUUID == null ? HEAD_UUID : this.profileUUID;
        final GameProfile profile = new GameProfile(uuid, "HEAD_PROFILE");
        profile.getProperties().put("textures", new Property("textures", this.texture));

        try {
            SKULL_METHOD.invoke(skullMeta, profile);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        stack.setItemMeta(skullMeta);
        return stack;
    }
}
