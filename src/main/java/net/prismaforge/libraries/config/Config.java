package net.prismaforge.libraries.config;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.NonNull;
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
        return save();
    }

    public boolean contains(String path) {
        return getConfig().contains(path);
    }

    @NonNull
    public Set<String> getKeyset(String path) {
        FileConfiguration cfg = this.getConfig();

        // Check if the path exists and is a section
        if (cfg.isConfigurationSection(path)) {
            return cfg.getConfigurationSection(path).getKeys(false)
                    .stream()
                    .filter(key -> cfg.isConfigurationSection(path + "." + key))
                    .collect(Collectors.toSet());
        }

        // Return an empty set if the path is not a section or does not exist
        return Collections.emptySet();
    }

    public void clean() {
        // Clear all entries from the in-memory FileConfiguration object
        this.fileConfiguration.getKeys(false).forEach(key -> this.fileConfiguration.set(key, null));

        // Save the cleared configuration to the YAML file to apply changes on disk
        save();
    }

    public void clean(String path) {
        FileConfiguration cfg = this.getConfig();

        // Check if the path exists and is a section
        if (cfg.isConfigurationSection(path)) {
            // Clear all keys in the specified section
            cfg.getConfigurationSection(path).getKeys(false).forEach(key -> cfg.set(path + "." + key, null));
        } else {
            System.out.println("The specified path does not exist or is not a section: " + path);
        }

        // Save the configuration to apply changes
        save();
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
