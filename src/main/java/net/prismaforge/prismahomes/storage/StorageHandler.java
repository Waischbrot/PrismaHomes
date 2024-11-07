package net.prismaforge.prismahomes.storage;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.prismahomes.PrismaHomes;
import net.prismaforge.prismahomes.utility.LangKey;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class StorageHandler {
    PrismaHomes plugin;
    Map<UUID, DataPlayer> dataCache;
    Map<String, String> messageCache;
    @NonFinal Config messages;

    public StorageHandler(final PrismaHomes plugin) {
        this.plugin = plugin;
        this.dataCache = new HashMap<>();
        this.messageCache = new HashMap<>();
    }

    public void reload() {
        createMessageConfig();
        this.dataCache.clear();
        this.messageCache.clear();
    }

    public void createMessageConfig() {
        this.messages = new Config("messages", plugin);

        //initialize all lang keys to config once!
        if (!messages.contains("messages.prefix.homes")) {
            for (final LangKey langKey : LangKey.values()) {
                langKey.translate();
            }
        }
    }

    @NonNull
    public synchronized String getMessage(String key, String defaultValue) {
        if (this.messageCache.containsKey(key)) {
            return this.messageCache.get(key);
        }
        final String result = this.messages.getConfigField(key, defaultValue);
        this.messageCache.put(key, result);
        return result;
    }

    @NonNull
    public synchronized DataPlayer get(final UUID uuid) {
        if (this.dataCache.containsKey(uuid)) {
            return this.dataCache.get(uuid);
        }
        final Config file = getFile(uuid);
        final DataPlayer data = DataPlayer.createFromConfig(file, uuid);
        this.dataCache.put(uuid, data);
        return data;
    }

    public synchronized void save(final DataPlayer data) {
        this.dataCache.put(data.uuid(), data);
        final Config file = getFile(data.uuid());
        data.saveToConfig(file);
    }

    private synchronized Config getFile(final UUID uuid) {
        return new Config("homes/" + uuid, this.plugin);
    }
}
