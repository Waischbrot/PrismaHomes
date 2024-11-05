package net.prismaforge.libraries.scheduler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class Scheduler {
    final JavaPlugin plugin;
    final List<Function<BukkitRunnable, Boolean>> pauseFilters;
    final List<Function<BukkitRunnable, Boolean>> stopFilters;
    BukkitRunnable runnable;
    long after;
    long every;
    long limiter;
    boolean async;

    public Scheduler(final @NonNull JavaPlugin plugin, boolean async) {
        this.plugin = plugin;
        this.async = async;
        this.pauseFilters = new LinkedList<>();
        this.stopFilters = new LinkedList<>();
        this.limiter = -1;
    }

    @NonNull
    public Scheduler async(boolean async) {
        this.async = async;
        return this;
    }

    @NonNull
    public Scheduler limit(long limiter) {
        this.limiter = limiter;
        return this;
    }

    @NonNull
    public Scheduler after(long after) {
        this.after = after;
        return this;
    }

    @NonNull
    public Scheduler after(long after, @NonNull TimeUnit timeUnit) {
        this.after = timeUnit.toMillis(after) / 50;
        return this;
    }

    @NonNull
    public Scheduler after(@NonNull Duration duration) {
        this.after = duration.toMillis() / 50;
        return this;
    }

    @NonNull
    public Scheduler every(long every) {
        this.every = every;
        return this;
    }

    @NonNull
    public Scheduler every(long every, @NonNull TimeUnit timeUnit) {
        this.every = timeUnit.toMillis(every) / 50;
        return this;
    }

    @NonNull
    public Scheduler every(@NonNull Duration duration) {
        this.every = duration.toMillis() / 50;
        return this;
    }

    @NonNull
    public Scheduler pauseIf(@NonNull Function<BukkitRunnable, Boolean> pauseFilter) {
        this.pauseFilters.add(pauseFilter);
        return this;
    }

    @NonNull
    public Scheduler stopIf(@NonNull Function<BukkitRunnable, Boolean> stopFilter) {
        this.stopFilters.add(stopFilter);
        return this;
    }

    public synchronized void cancel() {
        if (this.runnable != null) {
            this.runnable.cancel();
        }
    }

    public synchronized int run(final @NonNull Runnable runnable) {
        return this.run(consumer -> runnable.run());
    }

    public synchronized int run(final @NonNull Consumer<BukkitRunnable> taskConsumer) {
        this.runnable = new BukkitRunnable() {

            @Override
            public void run() {
                for (Function<BukkitRunnable, Boolean> pauseFilter : pauseFilters) {
                    if (pauseFilter.apply(this)) {
                        return;
                    }
                }
                for (Function<BukkitRunnable, Boolean> stopFilter : stopFilters) {
                    if (stopFilter.apply(this)) {
                        this.cancel();
                        return;
                    }
                }
                taskConsumer.accept(this);
                if (limiter >= 0) {
                    limiter--;
                    if (limiter <= 0) {
                        this.cancel();
                    }
                }
            }

        };

        if (this.async) {
            if (this.every <= 0) {
                return this.runnable.runTaskLaterAsynchronously(this.plugin, this.after).getTaskId();
            } else {
                return this.runnable.runTaskTimerAsynchronously(this.plugin, this.after, this.every).getTaskId();
            }
        } else {
            if (this.every <= 0) {
                return this.runnable.runTaskLater(this.plugin, this.after).getTaskId();
            } else {
                return this.runnable.runTaskTimer(this.plugin, this.after, this.every).getTaskId();
            }
        }
    }
}
