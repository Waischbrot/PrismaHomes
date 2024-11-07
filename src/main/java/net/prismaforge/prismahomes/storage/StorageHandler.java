package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.prismahomes.PrismaHomes;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class StorageHandler {
    PrismaHomes plugin;

    @NonNull
    public synchronized DataPlayer get(final UUID uuid) {
        final Config file = getFile(uuid);
        return DataPlayer.createFromConfig(file, uuid);
    }

    public synchronized void save(final DataPlayer data) {
        final Config file = getFile(data.uuid());
        data.saveToConfig(file);
    }

    private synchronized Config getFile(final UUID uuid) {
        return new Config("homes/" + uuid, this.plugin);
    }
}
