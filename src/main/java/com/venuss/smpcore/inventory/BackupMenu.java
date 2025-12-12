package com.venuss.smpcore.inventory;

import com.venuss.smpcore.models.BackupInfo;
import com.venuss.smpcore.util.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BackupMenu implements InventoryHolder {

    private final Inventory inventory;
    private final List<BackupInfo> backups;

    public BackupMenu(MiniMessage mm, List<BackupInfo> backupInfoList) {
        this.inventory = Bukkit.createInventory(this, 9, mm.deserialize("<gold><b>Backup Menu</b></gold>"));
        int i = 0;
        for (BackupInfo backupInfo : backupInfoList) {
            ItemStack is = new ItemStack(Material.CHEST);
            is.editMeta(meta -> meta.displayName(mm.deserialize(String.format("<gold><b>Backup #%d</b></gold>", backupInfo.backupId().intValue()))));
            ItemStack oneBackup = new ItemBuilder(Material.CHEST)
                    .name(String.format("<gray>Backup <blue>#%d</blue>", backupInfo.backupId().intValue()))
                    .build();
            this.inventory.setItem(i, is);
            i++;
        }
        this.backups = backupInfoList;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
