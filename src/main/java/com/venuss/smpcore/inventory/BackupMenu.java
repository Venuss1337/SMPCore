package com.venuss.smpcore.inventory;

import com.venuss.smpcore.menu.PagedMenu;
import com.venuss.smpcore.models.BackupInfo;
import com.venuss.smpcore.util.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BackupMenu extends PagedMenu {

    private static final int[] INNER_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final MiniMessage mm;
    private final String playerNickname;

    public BackupMenu(MiniMessage mm, List<BackupInfo> backupInfoList) {
        super(54, mm.deserialize("<blue>Backup Menu"), INNER_SLOTS.length);
        
        this.mm = mm;
        this.playerNickname = backupInfoList.isEmpty() ? "" : backupInfoList.getFirst().nickname();

        List<ItemStack> backupItems = new ArrayList<>();
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
            backupItems.add(singleBackup);
            i++;
        }
        
        setItems(backupItems);
        fillMenu();
    }
    
    /*
     * Fills the menu with borders, navigation, and backup slots
     */
    public void fillMenu() {
        // Clear all and fill borders
        clear();
        fillBorders();
        fillNavigationSlots();
        fillBackupSlots();
        update(); // Apply all buffered changes at once
    }

    /**
     * Fills the border slots with decorative items
     */
    private void fillBorders() {
        ItemStack borderItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
        
        int size = 54;
        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || i % 9 == 8) {
                setItem(i, borderItem);
            }
        }
    }

    /*
     * Fills the navigation slots
     */
    private void fillNavigationSlots() {
        for (int i = 47; i <= 51; i++) {
            setItem(i, null);
        }
        setItem(49, new ItemBuilder(Material.NETHER_STAR)
                .name("<blue>Informations")
                .lore(
                        String.format("<gray>Player: <blue>%s", this.playerNickname),
                        String.format("<gray>Current page: <blue>%d", getCurrentPage() + 1)
                )
                .build());
        if (hasNextPage()) {
            setItem(51, new ItemBuilder(Material.ARROW).name("<blue>Next page").build());
        }
        if (hasPreviousPage()) {
            setItem(47, new ItemBuilder(Material.ARROW).name("<blue>Previous page").build());
        }
    }
    
    private void fillBackupSlots() {
        for (var slot : INNER_SLOTS) {
            setItem(slot, null);
        }

        List<ItemStack> pageItems = getCurrentPageItems();
        for (int i = 0; i < pageItems.size() && i < INNER_SLOTS.length; i++) {
            setItem(INNER_SLOTS[i], pageItems.get(i));
        }
    }

    @Override
    protected void onPageChange() {
        fillMenu();
    }

    @Override
    public void handleClick(@NotNull InventoryClickEvent event) {
        int slot = event.getSlot();
        ItemStack clickedItem = getItem(slot);
        
        if (clickedItem == null) {
            return;
        }

        switch (clickedItem.getType()) {
            case CHEST -> {
                // TODO: Open backup player menu
                BackupPlayerMenu backupPlayerMenu = new BackupPlayerMenu(mm, event.getWhoClicked());
                event.getWhoClicked().closeInventory();
                event.getWhoClicked().openInventory(backupPlayerMenu.getInventory());
            }
            case ARROW -> {
                if (slot == 51) {
                    nextPage();
                } else if (slot == 47) {
                    previousPage();
                }
            }
        }
    }
}
