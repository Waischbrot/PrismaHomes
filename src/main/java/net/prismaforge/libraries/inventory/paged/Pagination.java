package net.prismaforge.libraries.inventory.paged;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.prismaforge.libraries.inventory.basic.Button;
import net.prismaforge.libraries.inventory.basic.PrismaInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Pagination {
    PrismaInventory prismaInventory;
    LinkedList<Integer> slots;
    LinkedList<Button> buttons;
    @NonFinal int page;

    public Pagination(final PrismaInventory inventory) {
        this.prismaInventory = inventory;
        this.slots = new LinkedList<>();
        this.buttons = new LinkedList<>();
    }

    public void registerPageSlots(final Integer... slots) {
        registerPageSlots(Arrays.asList(slots));
    }

    public void registerPageSlots(final List<Integer> slots) {
        this.slots.addAll(slots);
    }

    public void registerPageSlotsBetween(int slot1, int slot2) {
        if (slot1 > slot2) return;
        for (; slot1 <= slot2; slot1++) {
            this.slots.add(slot1);
        }
    }

    public void unregisterAllSlots() {
        this.slots.clear();
    }

    public int getCurrentPage() {
        return page;
    }

    public Pagination nextPage() {
        if (page >= getLastPage()) return this;
        page += 1;
        return this;
    }

    public Pagination previousPage() {
        if (page <= 0) return this;
        page -= 1;
        return this;
    }

    public Pagination firstPage() {
        page = 0;
        return this;
    }

    public Pagination lastPage() {
        page = getLastPage();
        return this;
    }

    public boolean isLastPage() {
        return page == getLastPage();
    }

    public boolean isFirstPage() {
        return page == 0;
    }

    public int getLastPage() {
        if (slots.isEmpty()) return 0;
        return Math.max((buttons.size() - 1) / slots.size(), 0);
    }


    public void addButton(final @NonNull Button... buttons) {
        this.buttons.addAll(Arrays.asList(buttons));
    }

    public void addButton(final @NonNull ItemStack... items) {
        Arrays.stream(items)
                .map(Button::new)
                .forEachOrdered(this.buttons::add);
    }

    public void addButton(final @NonNull Material... materials) {
        Arrays.stream(materials)
                .map(Button::new)
                .forEachOrdered(this.buttons::add);
    }

    @NonNull
    public List<Button> getButtons() {
        return buttons;
    }

    public void update() {
        if (this.page < 0) return;

        for (int slotNumber = 0; slotNumber < slots.size(); slotNumber++) {
            int buttonNumber = slotNumber + page * slots.size();

            if (buttons.size() <= buttonNumber) {
                prismaInventory.removeButton(slots.get(slotNumber));
            } else {
                prismaInventory.addButton(slots.get(slotNumber), buttons.get(buttonNumber));
            }
        }
    }

    public void clear() {
        this.buttons.clear();
    }
}