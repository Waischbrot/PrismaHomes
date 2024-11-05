package net.prismaforge.libraries.items.sub;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import net.prismaforge.libraries.strings.ColorUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnegative;
import java.util.*;

@SuppressWarnings("unused")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class ItemCreator {
    Material type;
    String name;
    int amount;
    int customModelData;
    boolean glow;
    boolean unbreakable;
    boolean hideToolTip;
    List<String> lore;  //careful -> this is nullable
    Set<ItemFlag> itemFlags; //careful -> this is nullable
    Map<Enchantment, Integer> enchantments; //careful -> this is nullable

    /**
     * Create a new ItemCreator with the given ItemType
     */
    public ItemCreator(@NonNull Material type) {
        this(type, 1);
    }

    /**
     * Create a new ItemCreator with the given ItemType & Amount
     */
    public ItemCreator(@NonNull Material type, @Nonnegative int amount) {
        this.type = type;
        this.name = "";
        this.amount = amount;
        this.customModelData = 0;
        this.glow = false;
        this.unbreakable = false;
        this.hideToolTip = false;
        this.lore = null;
        this.itemFlags = null;
        this.enchantments = null;
    }

    /**
     * Used for copying an existing ItemCreator to alter it without modifying the original!
     */
    public ItemCreator(ItemCreator itemCreator) {
        this.type = itemCreator.getType();
        this.name = itemCreator.getName();
        this.amount = itemCreator.getAmount();
        this.customModelData = itemCreator.getCustomModelData();
        this.glow = itemCreator.isGlow();
        this.unbreakable = itemCreator.isUnbreakable();
        this.hideToolTip = itemCreator.isHideToolTip();
        this.lore = itemCreator.getLore();
        this.itemFlags = itemCreator.getItemFlags();
        this.enchantments = itemCreator.getEnchantments();
    }

    /**
     * This method tries to copy everything relevant from the given ItemStack and therefore
     * allow to edit it. Might face difficulties when using ItemStack's with unrelated Meta's!
     */
    public ItemCreator(ItemStack itemStack) {
        final ItemMeta meta = itemStack.getItemMeta();
        this.type = itemStack.getType();
        this.name = itemStack.hasItemMeta() ? meta.getDisplayName() : "";
        this.amount = itemStack.getAmount();
        this.customModelData = itemStack.hasItemMeta() ? meta.getCustomModelData() : 0;
        this.glow = false;
        this.unbreakable = itemStack.hasItemMeta() && meta.isUnbreakable();
        this.hideToolTip = itemStack.hasItemMeta() && meta.isHideTooltip();
        this.lore = itemStack.hasItemMeta() ? meta.getLore() : null;
        this.itemFlags = itemStack.getItemMeta().getItemFlags();
        this.enchantments = itemStack.getEnchantments();
    }

    @NonNull
    public ItemCreator type(final Material type) {
        this.type = type;
        return this;
    }

    @NonNull
    public ItemCreator name(final String name) {
        this.name = name;
        return this;
    }

    @NonNull
    public ItemCreator amount(final int amount) {
        this.amount = Math.min(64, Math.max(1, amount)); //prevent incorrect amounts being entered here!
        return this;
    }

    /**
     * Change the CustomModelData of the target item.
     * @param customModelData CustomModelData to set, enter 0 to reset!
     * @return This ItemCreator Object with the given changes!
     */
    @NonNull
    public ItemCreator modelData(final int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    @NonNull
    public ItemCreator glow(final boolean glow) {
        this.glow = glow;
        return this;
    }

    @NonNull
    public ItemCreator glow() {
        this.glow = true;
        return this;
    }

    @NonNull
    public ItemCreator unbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    @NonNull
    public ItemCreator unbreakable() {
        this.unbreakable = true;
        return this;
    }

    @NonNull
    public ItemCreator hideToolTip(final boolean hideToolTip) {
        this.hideToolTip = hideToolTip;
        return this;
    }

    @NonNull
    public ItemCreator hideToolTip() {
        this.hideToolTip = true;
        return this;
    }

    @NonNull
    public ItemCreator lore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    @NonNull
    public ItemCreator lore(final String... lore) {
        this.lore = new ArrayList<>(List.of(lore));
        return this;
    }

    @NonNull
    public ItemCreator clearLore() {
        this.lore = null;
        return this;
    }

    @NonNull
    public ItemCreator flag(final @NonNull ItemFlag... flags) {
        if (this.itemFlags == null) this.itemFlags = new HashSet<>();
        this.itemFlags.addAll(Arrays.asList(flags));
        return this;
    }

    @NonNull
    public ItemCreator unflag(@NonNull ItemFlag... flags) {
        if (this.itemFlags == null) this.itemFlags = new HashSet<>();
        for (ItemFlag flag : flags)
            this.itemFlags.remove(flag);
        return this;
    }

    @NonNull
    public ItemCreator enchant(final @NonNull Enchantment enchantment, final int level) {
        if (this.enchantments == null) this.enchantments = new HashMap<>();
        this.enchantments.put(enchantment, level);
        return this;
    }

    @NonNull
    public ItemCreator disenchant(final @NonNull Enchantment enchantment) {
        if (this.enchantments == null) this.enchantments = new HashMap<>();
        this.enchantments.remove(enchantment);
        return this;
    }

    @NonNull
    public ItemCreator disenchant() {
        if (this.enchantments != null) {
            this.enchantments.clear();
        }
        return this;
    }

    /**
     * Builds the ItemStack this creator is specified to create!
     * @return Finished ItemStack
     */
    @NonNull
    public ItemStack build() {
        final ItemStack stack = new ItemStack(this.type, this.amount);
        final ItemMeta meta = stack.getItemMeta();

        //meta modification
        if (meta == null) return stack;
        meta.setUnbreakable(this.unbreakable);
        meta.setDisplayName(ColorUtil.colorString(this.name));
        if (this.lore != null) {
            final List<String> coloredLore = new ArrayList<>();
            this.lore.forEach(string -> coloredLore.add(ColorUtil.colorString(string)));
            meta.setLore(coloredLore);
        }
        if (this.customModelData != 0) meta.setCustomModelData(this.customModelData);
        if (this.itemFlags != null) this.itemFlags.forEach(meta::addItemFlags);
        if (this.enchantments != null) this.enchantments.forEach((key, value) -> meta.addEnchant(key, value, true));
        meta.setEnchantmentGlintOverride(this.glow);
        meta.setHideTooltip(this.hideToolTip);

        stack.setItemMeta(meta);
        return stack;
    }
}
