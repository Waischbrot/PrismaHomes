package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.libraries.scheduler.Scheduler;
import net.prismaforge.prismahomes.PrismaHomes;
import org.bukkit.Bukkit;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class StorageHandler {
    PrismaHomes plugin;

    @NonNull
    public synchronized DataPlayer get(final UUID uuid) {
        final Config file = new Config("homes/" + uuid.toString(), this.plugin);
        return DataPlayer.createFromConfig(file, uuid);
    }

    public synchronized void save(final DataPlayer data) {
        final Config file = new Config("homes/" + data.uuid(), this.plugin);
        data.saveToConfig(file);
    }
}
