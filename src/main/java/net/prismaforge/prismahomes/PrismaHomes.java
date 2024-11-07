package net.prismaforge.prismahomes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.PrismaLib;
import net.prismaforge.libraries.commands.CommandRegister;
import net.prismaforge.prismahomes.commands.HomeCommand;
import net.prismaforge.prismahomes.commands.SetHomeCommand;
import net.prismaforge.prismahomes.listener.ChatListener;
import net.prismaforge.prismahomes.storage.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class PrismaHomes extends JavaPlugin {
    private static StorageHandler STORAGE;

    @Override
    public void onEnable() {
        PrismaLib.initialize(this);

        STORAGE = new StorageHandler(this);
        STORAGE.createMessageConfig();

        CommandRegister.register(
                new HomeCommand(this),
                new SetHomeCommand(this)
        );

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    @NonNull
    public static StorageHandler STORAGE() {
        return STORAGE;
    }
}
