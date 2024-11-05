package net.prismaforge.libraries.scheduler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.PrismaLib;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class EmbeddedScheduler implements Runnable {
    Scheduler scheduler;

    public EmbeddedScheduler(int arg1, int arg2, boolean async) {
        this.scheduler = new Scheduler(PrismaLib.PLUGIN, async).after(arg1).every(arg2);
    }

    @NonNull
    public EmbeddedScheduler start() {
        this.scheduler.run(this);
        return this;
    }

    public void cancel() {
        scheduler.cancel();
    }
}
