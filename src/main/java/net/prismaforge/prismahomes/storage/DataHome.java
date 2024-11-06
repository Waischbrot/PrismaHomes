package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.serializer.StringSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter @Setter
public final class DataHome implements Serializable {
    final String key;
    String displayName;
    String material;
    String world;
    double x;
    double y;
    double z;
    float yaw;
    float pitch;

    public DataHome(String key, String world, double x, double y, double z, float yaw, float pitch) {
        this.key = key;
        this.displayName = "&e" + key;
        this.material = "CAMPFIRE";
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @NonNull
    public String serialize() {
        try {
            return StringSerializer.toString(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public static Optional<DataHome> deserialize(final String serialized) {
        try {
            final DataHome home = (DataHome) StringSerializer.fromString(serialized);
            return Optional.of(home);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
