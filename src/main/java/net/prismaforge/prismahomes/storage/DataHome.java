package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter @Setter
public final class DataHome {
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
        this.displayName = "<yellow>" + key;
        this.material = "CAMPFIRE";
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
