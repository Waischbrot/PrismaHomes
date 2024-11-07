package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@AllArgsConstructor
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
}
