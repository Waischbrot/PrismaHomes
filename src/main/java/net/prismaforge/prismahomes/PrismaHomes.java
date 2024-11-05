package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class PrismaHomes extends JavaPlugin {
    Config config;

    @Override
    public void onEnable() {
        this.config = new Config("config", this);
    }

    @Override
    public void onDisable() {

    }
}
