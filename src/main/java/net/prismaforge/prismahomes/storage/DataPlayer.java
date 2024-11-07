package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter @Setter
public final class DataPlayer {
    final UUID uuid;
    final List<DataHome> homes;

    public DataPlayer(final UUID uuid) {
        this.uuid = uuid;
        this.homes = new ArrayList<>();
    }

    private DataPlayer(final UUID uuid, final List<DataHome> homes) {
        this.uuid = uuid;
        this.homes = homes;
    }

    @NonNull
    public static DataPlayer createFromConfig(final Config config, final UUID uuid) {
        // First creation
        if (!config.contains("uuid")) {
            DataPlayer data = new DataPlayer(uuid, new ArrayList<>());
            data.saveToConfig(config);
            return data;
        }

        // Current version -> load all homes from config
        final List<DataHome> homes = new ArrayList<>();
        for (final String key : config.getKeyset("homes")) {
            final String prefix = "homes." + key + ".";
            final String displayName = config.getConfigField(prefix + "display-name");
            final String material = config.getConfigField(prefix + "material");
            final String world = config.getConfigField(prefix + "world");

            // Retrieve as Double and cast to double (primitive)
            final double x = config.getConfigField(prefix + "x");
            final double y = config.getConfigField(prefix + "y");
            final double z = config.getConfigField(prefix + "z");

            // Retrieve as Double and convert to Float
            final float yaw = ((Number) config.getConfigField(prefix + "yaw", 0.0)).floatValue();
            final float pitch = ((Number) config.getConfigField(prefix + "pitch", 0.0)).floatValue();

            homes.add(new DataHome(key, displayName, material, world, x, y, z, yaw, pitch));
        }
        return new DataPlayer(uuid, homes);
    }


    public void saveToConfig(final Config config) {
        config.setField("uuid", uuid.toString());
        config.clean("homes"); //delete old homes and overwrite clean!
        for (final DataHome home : this.homes) {
            final String prefix = "homes." + home.key() + ".";
            config.setField(prefix + "display-name", home.displayName());
            config.setField(prefix + "material", home.material());
            config.setField(prefix + "world", home.world());
            config.setField(prefix + "x", home.x());
            config.setField(prefix + "y", home.y());
            config.setField(prefix + "z", home.z());
            config.setField(prefix + "yaw", home.yaw());
            config.setField(prefix + "pitch", home.pitch());
        }
    }

    @NonNull
    public Optional<DataHome> findHome(final String key) {
        for (final DataHome home : this.homes) {
            if (home.key().equalsIgnoreCase(key)) {
                return Optional.of(home);
            }
        }
        return Optional.empty();
    }

    public void createHome(final DataHome home) {
        this.homes.add(home);
    }

    public void deleteHome(final String key) {
        findHome(key).ifPresent(this.homes::remove);
    }

    public int countHomes() {
        return this.homes.size();
    }
}
