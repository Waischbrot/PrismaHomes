package net.prismaforge.libraries.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class Skin {
    private static final Pattern PATTERN = Pattern.compile("\"(http://textures\\.minecraft\\.net/texture/)(?<shortTexture>\\w+)\"");
    public static final Skin STEVE = new Skin(
            "ewogICJ0aW1lc3RhbXAiIDogMTY2MTY3OTM3OTkwMCwKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5" +
                    "NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGV2ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR" +
                    "1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dH" +
                    "VyZS82MGE1YmQwMTZiM2M5YTFiOTI3MmU0OTI5ZTMwODI3YTY3YmU0ZWJiMjE5MDE3YWRiYmM0YTRkMjJlYmQ1YjEiCiAgICB9L" +
                    "AogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk1M2Nh" +
                    "YzhiNzc5ZmU0MTM4M2U2NzVlZTJiODYwNzFhNzE2NThmMjE4MGY1NmZiY2U4YWEzMTVlYTcwZTJlZDYiCiAgICB9CiAgfQp9",
            "ucw8h2rj66lroaieZON3VyQRbSLDdZzS/lOiAIrNbcKgsrKuD8UAiC9daUiRJ7aQCilOhCnnKVwwXX74PmHWL4nAPpPb903ZHwh" +
                    "mRS8Mshc0mPZrpvWfpkoZhDVdiFpqHSfGdwRweZyCifa39DRJstAuPmqdp3Ioyot6Rx4bCauXV8WQq0yMgP+oDrkx8O2aBr19h6" +
                    "6OXDl7Er28e8IgDnNHAcanSCNnWgV/RYiBqIzBmItLescpyTqCnVl3uYZfXVyEvNEy5IIBM6nhV4VoP9sf8Ld0AF6bXsSCaMbaJ" +
                    "8h99+jqCUlSGbFuMYlU8Ih0us9vvaAolqQasjvNxpXbN390mU3lw3NupCzSNNG+47Os5XeZ8C6nkY1kq3eHTNW/hDYwIi1A2TQv" +
                    "qvmeU0dMclV9L/Oj85YA1RbCYkK/3DuQBf10rek26EiOL1qyBL5A8jBorK5mR2ZWzDgWorN9XXclwPnadt+nN80m+fDuZBGS6yY" +
                    "Rljc/gqckTBprd055Vk93K3LmqBKS87nIILt9QRQO8x8rLYMZ4uz0Kwzagj/Gb4V7FHtZ4VMyzcqS9iYJfGGzBuvL72CzkAM96P" +
                    "YXO4PjSTgzQ4FwlMkNndcyt4TecwcrmfKpusW1j4i4QM092ktbDvMt9LFVCZkVRqnsiN/va5/RnNBfLJpcp7IzLLU=");
    private static final Map<String, Skin> SKIN_CACHE = new HashMap<>();

    @NonNull
    public static Skin fromName(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            return fromUUID(uuid);
        } catch (Exception ignored) {
            return STEVE;
        }
    }

    @NonNull
    public static Skin fromUUID(UUID uuid) {
        return fromUUID(uuid.toString());
    }

    @NonNull
    public static Skin fromUUID(String uuid) {
        if (SKIN_CACHE.containsKey(uuid)) return SKIN_CACHE.get(uuid);
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();
            Skin skin = new Skin(texture, signature);
            SKIN_CACHE.put(uuid, skin);
            return skin;
        } catch (Exception ignored) {
            return STEVE;
        }
    }

    String texture;
    String signature;
    String shortTexture;

    public Skin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
        Matcher matcher = PATTERN.matcher(new String(Base64.getDecoder().decode(texture.getBytes())));
        this.shortTexture = matcher.find() ? matcher.group("shortTexture") : "";
    }
}
