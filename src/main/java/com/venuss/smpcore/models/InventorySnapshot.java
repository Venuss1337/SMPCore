package com.venuss.smpcore.models;

import com.venuss.smpcore.util.InventorySerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventorySnapshot {

    private final ItemStack[] fullContents;

    // Standard Minecraft Inventory Constants
    private static final int SLOT_HOTBAR_START = 0;
    private static final int SLOT_HOTBAR_END = 8;
    private static final int SLOT_STORAGE_START = 9;
    private static final int SLOT_STORAGE_END = 35;
    private static final int SLOT_BOOTS = 36;
    private static final int SLOT_LEGGINGS = 37;
    private static final int SLOT_CHESTPLATE = 38;
    private static final int SLOT_HELMET = 39;
    private static final int SLOT_OFFHAND = 40;

    public InventorySnapshot(byte[] compressedData) {
        this.fullContents = InventorySerializer.fromCompressedBytes(compressedData);
    }

    public ItemStack[] getHotbarSlots() {
        return Arrays.copyOfRange(fullContents, SLOT_HOTBAR_START, SLOT_HOTBAR_END + 1);
    }

    public ItemStack[] getStorageSlots() {
        return Arrays.copyOfRange(fullContents, SLOT_STORAGE_START, SLOT_STORAGE_END + 1);
    }

    public ItemStack getOffHand() {
        if (fullContents.length <= SLOT_OFFHAND) return new ItemStack(Material.AIR);
        return fullContents[SLOT_OFFHAND];
    }

    public ArmorItems getArmor() {
        return new ArmorItems(fullContents);
    }

    /**
     * Helper class specifically for Armor retrieval
     */
    public class ArmorItems {
        private final ItemStack[] items;

        public ArmorItems(ItemStack[] items) {
            this.items = items;
        }

        public ItemStack getHelmet() {
            return getItemSafe(SLOT_HELMET);
        }

        public ItemStack getChestPlate() {
            return getItemSafe(SLOT_CHESTPLATE);
        }

        public ItemStack getLeggings() {
            return getItemSafe(SLOT_LEGGINGS);
        }

        public ItemStack getBoots() {
            return getItemSafe(SLOT_BOOTS);
        }

        private ItemStack getItemSafe(int index) {
            if (index >= items.length || items[index] == null) {
                return new ItemStack(Material.AIR);
            }
            return items[index];
        }
    }
}
