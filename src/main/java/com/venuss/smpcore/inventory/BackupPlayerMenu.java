package com.venuss.smpcore.inventory;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BackupPlayerMenu implements InventoryHolder {
    private final Inventory inventory;

    public BackupPlayerMenu(MiniMessage mm, Player player) {
        this.inventory = Bukkit.createInventory(this, 81, mm.deserialize(String.format("<gray>Backup <blue>%s", player.getName())));
        // TODO: implementation
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
