package com.venuss.smpcore.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryHelper {

    public static void fillBorders(Inventory inventory, Material material, int size) {
        ItemStack borderItem = new ItemBuilder(material).name(" ").build();

        int rows = size / 9;
        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItem(i, borderItem);
            }
        }
    }
}
