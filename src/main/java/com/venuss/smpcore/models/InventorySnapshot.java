package com.venuss.smpcore.models;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserInventoryData {
    private Map<Integer, ItemStack> mainInventory;
    private Map<Integer, ItemStack> hotbar;
    private Map<Integer, ItemStack> armor;
    private ItemStack offHand;

    public UserInventoryData(PlayerInventory inventory) {
        this.mainInventory = new HashMap<>();
        this.hotbar = new HashMap<>();
        this.armor = new HashMap<>();

        // the 27 sized main player storage.
        ItemStack[] storageContents = inventory.getStorageContents();
        for (int i = 0; i < Arrays.stream(storageContents).count(); i++) {
            if (storageContents[i] == null) {
                continue;
            }
            this.mainInventory.put(i, storageContents[i]);
        }
        ItemStack[] hotbarContents = inventory.getContents();
        for (int i = 0; i < 9; i++) {
            if (storageContents[i] == null) {
                continue;
            }
            this.hotbar.put(i, hotbarContents[i]);
        }
    }
}
