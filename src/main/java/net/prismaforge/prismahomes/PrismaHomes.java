package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.prismaforge.libraries.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PrismaHomes extends JavaPlugin {
    BukkitAudiences adventure;
    Config config;

    @Override
    public void onEnable() {
        this.config = new Config("config", this);
        this.adventure = BukkitAudiences.create(this);
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    @NonNull
    public BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @NonNull
    public Config config() {
        return this.config;
    }
}
