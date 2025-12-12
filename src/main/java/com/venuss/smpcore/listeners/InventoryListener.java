package com.venuss.smpcore.listeners;

import com.venuss.smpcore.inventory.BackupMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getHolder() instanceof BackupMenu) {
            // TODO: implementation
        }
    }
}
