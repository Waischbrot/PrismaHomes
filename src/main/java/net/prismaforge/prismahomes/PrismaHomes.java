package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.config.Config;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class PrismaHomes extends JavaPlugin {
    Config configuration;

    @Override
    public void onEnable() {
        this.configuration = new Config("config", this);
    }

    @Override
    public void onDisable() {

    }
}
