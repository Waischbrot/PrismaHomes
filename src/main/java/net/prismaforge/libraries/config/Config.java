package net.prismaforge.libraries.config;

import java.io.File;
import java.io.IOException;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Config {
    String path;
    File yamlFile;
    FileConfiguration fileConfiguration;

    //path can just be "default" or with a path like homes/xy (.yml is appended automatically)
    public Config(final String name, final JavaPlugin plugin) {
        this.path = "plugins" + File.separatorChar + plugin.getName();
        this.yamlFile = new File(path + File.separatorChar + name + ".yml");
        this.fileConfiguration = (FileConfiguration) YamlConfiguration.loadConfiguration(this.yamlFile);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getConfigField(String path) {
        FileConfiguration cfg = this.getConfig();
        Object o = cfg.get(path);
        if (o != null) {
            return (T) o;
        } else {
            cfg.set(path, "undefined");
            save();
        }
        return null;
    }

    public boolean setField(String path, Object o) {
        getConfig().set(path, o);
        getConfig().contains("");
        return save();
    }

    public boolean contains(String path) {
        return getConfig().contains(path);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T getConfigField(String path, T whenNotFound) {
        FileConfiguration cfg = this.getConfig();
        Object o = cfg.get(path);
        if (o != null) {
            try {
                return (T) o;
            } catch (Exception e) {
                return null;
            }
        } else {
            cfg.set(path, whenNotFound);
            save();
        }
        return whenNotFound;
    }

    private FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    private boolean save() {
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs();
        try {
            this.fileConfiguration.save(this.yamlFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
