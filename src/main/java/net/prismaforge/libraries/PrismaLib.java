package net.prismaforge.libraries;

import lombok.experimental.UtilityClass;
import net.prismaforge.libraries.inventory.basic.InventoryAPI;
import net.prismaforge.prismahomes.PrismaHomes;

@UtilityClass
public final class PrismaLib {
    public static PrismaHomes PLUGIN;

    public static void initialize(final PrismaHomes plugin) {
        PrismaLib.PLUGIN = plugin;
        InventoryAPI.init(PrismaLib.PLUGIN);
    }
}
