package com.venuss.smpcore.menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for creating custom menus/GUIs.
 * Implements deferred updates to prevent visual flickering when setting items.
 * Changes are buffered and only applied when update() is called.
 */
public abstract class Menu implements InventoryHolder {
    
    protected final Inventory inventory;
    private final Map<Integer, ItemStack> buffer;
    private boolean needsUpdate;
    
    /**
     * Creates a menu with the specified size and title.
     * 
     * @param size The size of the inventory (must be multiple of 9, max 54)
     * @param title The title component for the inventory
     */
    protected Menu(int size, net.kyori.adventure.text.Component title) {
        this.inventory = Bukkit.createInventory(this, size, title);
        this.buffer = new HashMap<>();
        this.needsUpdate = false;
    }
    
    /**
     * Sets an item in the menu at the specified slot.
     * The item is buffered and will only be visible after calling update().
     * 
     * @param slot The slot index (0-53)
     * @param item The item to set, or null to clear the slot
     */
    public void setItem(int slot, @Nullable ItemStack item) {
        buffer.put(slot, item);
        needsUpdate = true;
    }
    
    /**
     * Gets the item currently displayed at the specified slot.
     * This returns the live inventory item, not the buffered item.
     * 
     * @param slot The slot index
     * @return The item at the slot, or null if empty
     */
    @Nullable
    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }
    
    /**
     * Applies all buffered changes to the inventory.
     * This makes all setItem() calls since the last update() visible.
     */
    public void update() {
        if (!needsUpdate) {
            return;
        }
        
        for (Map.Entry<Integer, ItemStack> entry : buffer.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue());
        }
        
        buffer.clear();
        needsUpdate = false;
    }
    
    /**
     * Alternative name for update() to match common API conventions.
     */
    public void refresh() {
        update();
    }
    
    /**
     * Clears all items from the menu (buffered operation).
     */
    public void clear() {
        for (int i = 0; i < inventory.getSize(); i++) {
            setItem(i, null);
        }
    }
    
    /**
     * Handles click events for this menu.
     * This method is called by MenuListener when a player clicks in the inventory.
     * 
     * @param event The inventory click event
     */
    public abstract void handleClick(@NotNull InventoryClickEvent event);
    
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
