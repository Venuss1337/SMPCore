package com.venuss.smpcore.inventory;

import com.venuss.smpcore.models.BackupInfo;
import com.venuss.smpcore.util.InventoryHelper;
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

    private static final int[] INNER_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final Inventory inventory;
    private final ItemStack[] backups;
    private int currentPage = 0;
    private final int maxPages;
    private final String playerNickname;

    public BackupMenu(MiniMessage mm, List<BackupInfo> backupInfoList) {
        this.inventory = Bukkit.createInventory(this, 54, mm.deserialize("<blue>Backup Menu"));
        this.backups = new ItemStack[backupInfoList.size()];
        this.maxPages = backupInfoList.size() / INNER_SLOTS.length + 1;
        this.playerNickname = backupInfoList.getFirst().nickname();

        System.out.println(maxPages);

        int i = 0;
        for (BackupInfo backupInfo : backupInfoList) {
            ItemStack singleBackup = new ItemBuilder(Material.CHEST)
                    .name(String.format("<gray><b>Backup <blue>#%d</blue>", i+1))
                    .lore(
                            String.format("<gray>Date: <blue>%s", backupInfo.dateCreated()),
                            String.format("<gray>Cause: <blue>%s", backupInfo.deathCause()),
                            String.format("<gray>ID: <blue>%d", backupInfo.backupId().intValue())
                    )
                    .build();
            backups[i++] = singleBackup;
        }

        fillMenu();
    }
    /*
     * Firstly clears all the slots, then fills with proper elements
     */
    public void fillMenu() {
        // fill the whole to zero
        this.inventory.clear();
        InventoryHelper.fillBorders(this.inventory, Material.GRAY_STAINED_GLASS_PANE, 54);
        fillNavigationSlots();
        fillBackupSlots();
    }

    public void nextPage() {
        // If the current page is equal to max pages, then nextPage should not be called
        assert this.currentPage < this.maxPages;

        currentPage += 1;
        fillMenu();
    }

    public void previousPage() {
        // If the current page is 0, then previousPage should not be called
        assert this.currentPage > 0;

        currentPage -= 1;
        fillMenu();
    }

    /*
     * Fills the last slots for navigation
     */
    private void fillNavigationSlots() {
        for (int i = 47; i <= 51; i++) {
            this.inventory.clear(i);
        }
        this.inventory.setItem(49, new ItemBuilder(Material.NETHER_STAR)
                .name("<blue>Informations")
                .lore(
                        String.format("<gray>Player: <blue>%s", this.playerNickname),
                        String.format("<gray>Current page: <blue>%d", this.currentPage + 1)
                )
                .build());
        if (this.currentPage+1 != this.maxPages && this.maxPages > 0) {
            this.inventory.setItem(51, new ItemBuilder(Material.ARROW).name("<blue>Next page").build());
        }
        if (this.currentPage > 0 && this.maxPages > 0) {
            this.inventory.setItem(47, new ItemBuilder(Material.ARROW).name("<blue>Previous page").build());
        }
    }
    private void fillBackupSlots() {
        for (var slot : INNER_SLOTS) {
            this.inventory.clear(slot);
        }

        // 0, 28, 56, 112
        int helper = currentPage * INNER_SLOTS.length;
        for (var slot : INNER_SLOTS) {
            // length = 30 currentPage = 1 inner_slots.length = 28, helper = 29
            if (helper >= backups.length) return;
            this.inventory.setItem(slot, backups[helper++]);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
