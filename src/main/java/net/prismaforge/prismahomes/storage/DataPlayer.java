package net.prismaforge.prismahomes.storage;

import lombok.*;
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

    private DataPlayer(final Config config) {
        this.uuid = UUID.fromString(config.getConfigField("uuid"));
        this.homes = new ArrayList<>();
        final List<String> homes = config.getConfigField("homes");
        for (final String home : homes) {
            DataHome.deserialize(home).ifPresent(this.homes::add);
        }
    }

    @NonNull
    public static DataPlayer createFromConfig(final Config config, final UUID uuid) {
        if (!config.contains("uuid")) {
            config.setField("uuid", uuid.toString());
            config.setField("homes", List.of());
        }
        return new DataPlayer(config);
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
