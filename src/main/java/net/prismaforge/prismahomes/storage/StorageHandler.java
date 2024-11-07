package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.prismahomes.PrismaHomes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class StorageHandler {
    PrismaHomes plugin;
    Map<UUID, DataPlayer> cache;

    public StorageHandler(final PrismaHomes plugin) {
        this.plugin = plugin;
        this.cache = new HashMap<>();
    }

    @NonNull
    public synchronized DataPlayer get(final UUID uuid) {
        if (this.cache.containsKey(uuid)) {
            return this.cache.get(uuid);
        }
        final Config file = getFile(uuid);
        final DataPlayer data = DataPlayer.createFromConfig(file, uuid);
        this.cache.put(uuid, data);
        return data;
    }

    public synchronized void save(final DataPlayer data) {
        this.cache.put(data.uuid(), data);
        final Config file = getFile(data.uuid());
        data.saveToConfig(file);
    }

    private synchronized Config getFile(final UUID uuid) {
        return new Config("homes/" + uuid, this.plugin);
    }
}
