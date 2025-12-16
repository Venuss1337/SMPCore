package com.venuss.smpcore.inventory;

import com.venuss.smpcore.menu.Menu;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class BackupPlayerMenu extends Menu {
    
    public BackupPlayerMenu(MiniMessage mm, HumanEntity player) {
        super(81, mm.deserialize(String.format("<gray>Backup <blue>%s", player.getName())));
        // TODO: implementation
    }

    @Override
    public void handleClick(@NotNull InventoryClickEvent event) {
        // TODO: implementation
    }
}
