package net.prismaforge.libraries.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@SuppressWarnings("unused")
public final class PrismaSound {
    Sound sound;
    float volume;
    float pitch;

    public void play(final Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void play(final Player[] players) {
        for (Player player : players) {
            play(player);
        }
    }

    public void play(final List<Player> players) {
        for (Player player : players) {
            play(player);
        }
    }

    public void playAtLocation(final Player player, final Location location) {
        player.playSound(location, sound, volume, pitch);
    }

    public void playAtLocation(final Player[] players, final Location location) {
        for (Player player : players) {
            playAtLocation(player, location);
        }
    }

    public void playAtLocation(final List<Player> players, final Location location) {
        for (Player player : players) {
            playAtLocation(player, location);
        }
    }

    public void playAtLocations(final Player[] players, final Location[] locations) {
        for (Player player : players) {
            for (Location location : locations) {
                playAtLocation(player, location);
            }
        }
    }

    public void playAtLocations(final List<Player> players, final List<Location> locations) {
        for (Player player : players) {
            for (Location location : locations) {
                playAtLocation(player, location);
            }
        }
    }

    public void play() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            play(player);
        }
    }

    public void playAtLocation(final Location location) {
        if (location.getWorld() == null) {
            return;
        }
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    public void playAtLocations(final Location[] locations) {
        for (Location location : locations) {
            playAtLocation(location);
        }
    }

    public void playAtLocations(final List<Location> locations) {
        for (Location location : locations) {
            playAtLocation(location);
        }
    }


}
