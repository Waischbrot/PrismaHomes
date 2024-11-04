package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PrismaHomes extends JavaPlugin {
    Config config;

    @Override
    public void onEnable() {
        this.config = new Config("config", this);
    }

}
