package net.prismaforge.libraries.items;

import lombok.NonNull;
import net.prismaforge.libraries.items.sub.ItemCreator;
import net.prismaforge.libraries.items.sub.SkullCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class Items {
    private Items() {
        throw new AssertionError();
    }

    @NonNull
    public static ItemStack createItem(Material material, Consumer<ItemCreator> consumer) {
        final ItemCreator itemCreator = new ItemCreator(material);
        consumer.accept(itemCreator);
        return itemCreator.build();
    }

    @NonNull
    public static ItemStack createItem(Material material, int amount, Consumer<ItemCreator> consumer) {
        final ItemCreator itemCreator = new ItemCreator(material, amount);
        consumer.accept(itemCreator);
        return itemCreator.build();
    }

    public static void createItemAsync(Material material, Consumer<ItemCreator> consumer, Consumer<ItemStack> callback) {
        CompletableFuture.runAsync(() -> {
            final ItemCreator itemCreator = new ItemCreator(material);
            consumer.accept(itemCreator);
            callback.accept(itemCreator.build());
        });
    }

    public static void createItemAsync(Material material, int amount, Consumer<ItemCreator> consumer, Consumer<ItemStack> callback) {
        CompletableFuture.runAsync(() -> {
            final ItemCreator itemCreator = new ItemCreator(material, amount);
            consumer.accept(itemCreator);
            callback.accept(itemCreator.build());
        });
    }

    @NonNull
    public static ItemStack createSkull(Consumer<SkullCreator> consumer) {
        final SkullCreator skullCreator = new SkullCreator();
        consumer.accept(skullCreator);
        return skullCreator.build();
    }

    public static void createSkullAsync(Consumer<SkullCreator> consumer, Consumer<ItemStack> callback) {
        CompletableFuture.runAsync(() -> {
            final SkullCreator skullCreator = new SkullCreator();
            consumer.accept(skullCreator);
            callback.accept(skullCreator.build());
        });
    }

    /**
     * As of now, this method is deprecated because it is flagged experimental!
     */
    @Deprecated @NonNull
    public static ItemStack tryEditItem(ItemStack itemStack, Consumer<ItemCreator> consumer) {
        final ItemCreator itemCreator = new ItemCreator(itemStack);
        consumer.accept(itemCreator);
        return itemCreator.build();
    }
}
