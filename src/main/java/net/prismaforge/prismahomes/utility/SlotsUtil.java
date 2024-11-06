package net.prismaforge.prismahomes.utility;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public final class SlotsUtil {
    private static final int DEFAULT_HOMES = 1;

    public static int availableSlots(final Player player) {
        return player.getEffectivePermissions().stream()
                .filter(permission -> permission.getPermission().startsWith("prismahomes.slots."))
                .map(permission -> Integer.parseInt(permission.getPermission().replace("prismahomes.slots.", "")))
                .max(Integer::compareTo)
                .orElse(DEFAULT_HOMES);
    }
}
