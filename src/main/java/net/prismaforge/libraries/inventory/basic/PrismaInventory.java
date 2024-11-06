package net.prismaforge.libraries.inventory.basic;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.inventory.events.FakeCloseEvent;
import net.prismaforge.libraries.strings.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class PrismaInventory implements InventoryHolder {
    final String id;
    final InventoryType inventoryType;
    public final Player player;
    final Map<Integer, Button> buttons;
    final List<BukkitTask> taskList;
    Inventory inventory;
    @Setter String title;
    @Setter int size;
    @Setter boolean closed;

    public PrismaInventory(final Player player, final String id, final String title, final @Nonnegative int rows) {
        this(player, id, title, InventoryType.CHEST, 9 * rows);
    }

    public PrismaInventory(final Player player, final String id, final String title, final InventoryType inventoryType) {
        this(player, id, title,  inventoryType, Integer.MAX_VALUE);
    }

    private PrismaInventory(final Player player, final String id, final String title, final InventoryType inventoryType, final @Nonnegative int size) {
        this.player = player;
        this.size = size;
        this.title = title;
        this.id = id;
        this.inventoryType = inventoryType;
        this.closed = false;
        this.buttons = new HashMap<>();
        this.taskList = new ArrayList<>();
    }

    public boolean handleInventoryClickEvent(final InventoryClickEvent event) {
        return false;
    }

    public boolean handleInventoryDragEvent(final InventoryDragEvent event) {
        return false;
    }

    public void handleInventoryOpenEvent(final InventoryOpenEvent event) {

    }

    public void handleInventoryCloseEvent(final InventoryCloseEvent event) {
        if (event instanceof FakeCloseEvent) return;
        final PrismaInventory gui = InventoryAPI.getINSTANCE()
                .getInvFromInv(event.getPlayer().getOpenInventory().getTopInventory());
        if (gui == null) return;
        if (!gui.equals(this)) return;
        taskList.forEach(BukkitTask::cancel);
    }

    public void open() {
        final PrismaInventory gui = InventoryAPI.getINSTANCE().getPlayersInv(player);
        if (gui != null) {
            Bukkit.getPluginManager().callEvent(new FakeCloseEvent(player.getOpenInventory()));
        }
        InventoryAPI.getINSTANCE().getPlayers().put(player.getUniqueId(), this);
        if (this.inventoryType.equals(InventoryType.CHEST)) {
            inventory = Bukkit.createInventory(null, size, ColorUtil.colorString(title));
        } else {
            inventory = Bukkit.createInventory(null, inventoryType, ColorUtil.colorString(title));
        }
        player.openInventory(inventory);
    }

    public void openOldInventory() {
        if (inventory != null) {
            final PrismaInventory gui = InventoryAPI.getINSTANCE().getPlayersInv(player);
            if (gui != null) {
                Bukkit.getPluginManager().callEvent(new FakeCloseEvent(player.getOpenInventory()));
            }
            InventoryAPI.getINSTANCE().getPlayers().put(player.getUniqueId(), this);
            player.openInventory(inventory);
        }
    }


    //Tasks, die mit dem schließen gecancelt werden!
    public void runUpdateScheduler(@Nonnegative long runDelayInTicks,
                                   @Nonnegative long periodInTicks,
                                   @NonNull final Consumer<BukkitTask> update) {
        final BukkitTask[] bukkitTask = new BukkitTask[]{null};
        bukkitTask[0] = Bukkit.getScheduler().runTaskTimer(getPlugin(),
                () -> update.accept(bukkitTask[0]),
                runDelayInTicks, periodInTicks);
        taskList.add(bukkitTask[0]);
    }

    public void runTaskLater(@Nonnegative long runDelayInTicks,
                             @NonNull final Consumer<BukkitTask> update) {
        final BukkitTask[] bukkitTask = new BukkitTask[]{null};
        bukkitTask[0] = Bukkit.getScheduler().runTaskLater(getPlugin(),
                () -> update.accept(bukkitTask[0]),
                runDelayInTicks);
        taskList.add(bukkitTask[0]);
    }

    public void fillGui(final Button button) {
        for (int slot = 0; slot < size; slot++) {
            addButton(slot, button);
        }
    }

    public void fillGui(final ItemStack item) {
        fillGui(new Button(item));
    }

    public void fillGui(Material material) {
        fillGui(new Button(material));
    }

    public void fillGui(Button button, Iterable<Integer> notPlaceSlots) {
        for (int slot = 0; slot < size; slot++) {
            if (!contains(slot, notPlaceSlots)) {
                addButton(slot, button);
            }
        }
    }

    public void fillRow(Button button, int row) {
        for (int i = 0; i < 9; i++) {
            addButton((row * 9 + i), button);
        }
    }

    public void fillRow(ItemStack item, int row) {
        fillRow(new Button(item), row);
    }

    public void fillRow(Material material, int row) {
        fillRow(new Button(material), row);
    }

    public void fillColumn(Button button, int column) {
        for (int i = 0; i < (size / 9); i++) {
            addButton((i * 9 + column), button);
        }
    }

    public void fillColumn(ItemStack item, int column) {
        fillColumn(new Button(item), column);
    }

    public void fillColumn(Material material, int column) {
        fillColumn(new Button(material), column);
    }

    public void removeButton(final @Nonnegative int slot) {
        buttons.remove(slot);
        inventory.clear(slot);
    }

    public void addButton(@Nonnegative int slot, Button button) {
        if (inventory.getSize() <= slot) {
            throw new IndexOutOfBoundsException("Slot kann nicht höher als die Inventargröße sein! Inventargröße: " + inventory.getSize());
        }
        if (button == null) {
            throw new NullPointerException("Button kann nicht NULL sein!");
        }

        buttons.remove(slot);
        buttons.put(slot, button);
        inventory.setItem(slot, button.item());
    }

    public void addButtons(final @NonNull Button button, final @NonNull Integer... slots) {
        for (int slot : slots) {
            addButton(slot, button);
        }
    }

    public void addButtons(final @NonNull ItemStack itemStack, final @NonNull Integer... slots) {
        addButtons(new Button(itemStack), slots);
    }

    public void addButtons(final @NonNull Material material, final @NonNull Integer... slots) {
        addButtons(new Button(material), slots);
    }

    public void addButton(@Nonnegative int slot, ItemStack item) {
        addButton(slot, new Button(item));
    }

    public void addButton(ItemStack item) {
        addButton(inventory.firstEmpty(), new Button(item));
    }

    public void addButton(Material mat) {
        addButton(inventory.firstEmpty(), new Button(mat));
    }

    public void addButton(@Nonnegative int slot, Material mat) {
        addButton(slot, new Button(mat));
    }

    public void addButton(@Nonnegative int row, @Nonnegative int column, Button button) {
        addButton(((row * 9) + column), button);
    }

    @Override
    @NonNull
    public Inventory getInventory() {
        return inventory;
    }

    @NonNull
    public Plugin getPlugin() {
        return InventoryAPI.getINSTANCE().getPlugin();
    }

    @NonNull
    public Map<Integer, Button> getItems() {
        return buttons;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    public void sendTitleUpdate(final @NonNull String title) {
        setTitle(Objects.requireNonNull(title, "Titel kann nicht NULL sein!"));
        open();
    }

    public void sendSizeUpdate(@Nonnegative int size) {
        setSize(size);
        open();
    }

    private boolean contains(int i, Iterable<Integer> ints) {
        for (int number : ints) {
            if (number == i) {
                return true;
            }
        }
        return false;
    }
}
