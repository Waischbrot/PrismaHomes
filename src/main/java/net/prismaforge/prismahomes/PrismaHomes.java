package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.PrismaLib;
import net.prismaforge.libraries.commands.CommandRegister;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.prismahomes.commands.HomeCommand;
import net.prismaforge.prismahomes.commands.SetHomeCommand;
import net.prismaforge.prismahomes.storage.StorageHandler;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class PrismaHomes extends JavaPlugin {
    Config configuration;
    StorageHandler storageHandler;

    @Override
    public void onEnable() {
        PrismaLib.initialize(this);

        CommandRegister.register(
                new HomeCommand(this),
                new SetHomeCommand(this)
        );

        this.configuration = new Config("config", this);
        this.storageHandler = new StorageHandler(this);
    }

    @Override
    public void onDisable() {

    }
}
