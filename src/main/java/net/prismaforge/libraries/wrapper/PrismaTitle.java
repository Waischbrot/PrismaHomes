package net.prismaforge.libraries.wrapper;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnegative;
import java.util.List;

@SuppressWarnings("unused")
public final class PrismaTitle {

    @Getter private String title;
    @Getter private String subTitle;
    @Getter private int fadeIn;
    @Getter private int stay;
    @Getter private int fadeOut;

    public PrismaTitle(final @NonNull String title,
                       final @NonNull String subTitle,
                       int fadeIn,
                       int stay,
                       int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    //Builder
    @NonNull
    public PrismaTitle title(final @NonNull String title) {
        this.title = title;
        return this;
    }

    @NonNull
    public PrismaTitle subTitle(final @NonNull String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    @NonNull
    public PrismaTitle fadeIn(final @Nonnegative int fadeIn) {
        this.fadeIn = fadeIn;
        return this;
    }

    @NonNull
    public PrismaTitle stay(final @Nonnegative int stay) {
        this.stay = stay;
        return this;
    }

    @NonNull
    public PrismaTitle fadeOut(final @Nonnegative int fadeOut) {
        this.fadeOut = fadeOut;
        return this;
    }

    public void send(final @NonNull Player player) {
        player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
    }

    public void send(final Player @NonNull [] players) {
        for (Player player : players) {
            send(player);
        }
    }

    public void send(final @NonNull List<Player> players) {
        for (Player player : players) {
            send(player);
        }
    }

    public void sendToAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player);
        }
    }

    public void removeTitle(final @NonNull Player player) {
        player.sendTitle("", "", 0, 1, 0);
    }

    public void removeTitle(final Player @NonNull [] players) {
        for (Player player : players) {
            removeTitle(player);
        }
    }

    public void removeTitle(final @NonNull List<Player> players) {
        for (Player player : players) {
            removeTitle(player);
        }
    }

    public void removeTitleForAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeTitle(player);
        }
    }
}