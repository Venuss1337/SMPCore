package com.venuss.smpcore.listeners;

import com.venuss.smpcore.inventory.BackupMenu;
import com.venuss.smpcore.inventory.BackupPlayerMenu;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    private static MiniMessage mm;

    public InventoryListener(MiniMessage miniMessageInstance) {
        mm = miniMessageInstance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() instanceof BackupMenu backupMenu) {
            event.setCancelled(true);

            int slot = event.getSlot();
            var inventory = backupMenu.getInventory();

            if (inventory.getItem(slot) == null) return;


            switch(inventory.getItem(slot).getType()) {
                case Material.CHEST -> {
                    // TODO: impl
                    BackupPlayerMenu backupPlayerMenu = new BackupPlayerMenu(mm, (Player) event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(backupPlayerMenu.getInventory());
                }
                case Material.ARROW -> {
                    if (slot == 51) {
                        backupMenu.nextPage();
                    } else if (slot == 47) {
                        backupMenu.previousPage();
                    }
                }
            }
        }
    }
}
