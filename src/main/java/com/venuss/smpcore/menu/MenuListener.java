package com.venuss.smpcore.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Listener for handling menu click events.
 * Automatically cancels events and delegates to Menu instances to prevent item theft.
 */
public class MenuListener implements Listener {
    
    /**
     * Handles inventory click events.
     * If the clicked inventory belongs to a Menu, the event is cancelled
     * and delegated to the Menu's handleClick method.
     * 
     * @param event The inventory click event
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        
        InventoryHolder holder = event.getClickedInventory().getHolder();
        
        if (holder instanceof Menu menu) {
            // Cancel the event to prevent item theft
            event.setCancelled(true);
            
            // Delegate to the menu's click handler
            menu.handleClick(event);
        }
    }
}
