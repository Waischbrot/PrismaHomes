package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.PrismaLib;
import net.prismaforge.libraries.commands.CommandRegister;
import net.prismaforge.libraries.config.Config;
import net.prismaforge.prismahomes.commands.HomeCommand;
import net.prismaforge.prismahomes.commands.SetHomeCommand;
import net.prismaforge.prismahomes.listener.ChatListener;
import net.prismaforge.prismahomes.storage.StorageHandler;
import net.prismaforge.prismahomes.utility.LangKey;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class PrismaHomes extends JavaPlugin {
    Config configuration;
    StorageHandler storageHandler;

    @Override
    public void onEnable() {
        PrismaLib.initialize(this);

        this.configuration = new Config("config", this);
        this.storageHandler = new StorageHandler(this);

        CommandRegister.register(
                new HomeCommand(this),
                new SetHomeCommand(this)
        );

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        //initialize all lang keys to config once!
        if (!configuration.contains("messages.prefix.homes")) {
            for (final LangKey langKey : LangKey.values()) {
                langKey.translate(configuration);
            }
        }
    }

    @Override
    public void onDisable() {

    }
}
