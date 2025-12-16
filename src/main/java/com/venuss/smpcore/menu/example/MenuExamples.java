package com.venuss.smpcore.menu.example;

import com.venuss.smpcore.menu.Menu;
import com.venuss.smpcore.menu.PagedMenu;
import com.venuss.smpcore.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Example implementations demonstrating how to use the Menu API.
 * These are reference implementations showing best practices.
 */
public class MenuExamples {

    /**
     * Example 1: Simple Menu
     * A basic menu with deferred updates showing the buffer system.
     */
    public static class SimpleMenu extends Menu {
        
        public SimpleMenu(MiniMessage mm) {
            super(27, mm.deserialize("<gold>Simple Menu Example"));
            
            // All setItem calls are buffered
            setItem(10, new ItemBuilder(Material.DIAMOND).name("<blue>Click me!").build());
            setItem(11, new ItemBuilder(Material.EMERALD).name("<green>Or click me!").build());
            setItem(12, new ItemBuilder(Material.GOLD_INGOT).name("<yellow>Or me!").build());
            
            // Nothing is visible yet until we call update()
            // This prevents flickering when setting multiple items
            update();
        }
        
        @Override
        public void handleClick(@NotNull InventoryClickEvent event) {
            int slot = event.getSlot();
            ItemStack clicked = getItem(slot);
            
            if (clicked != null && event.getWhoClicked() instanceof Player player) {
                player.sendMessage(Component.text("You clicked slot " + slot + "!"));
                
                // Example: Change the item when clicked
                setItem(slot, new ItemBuilder(Material.BARRIER)
                        .name("<red>Already clicked!")
                        .build());
                update(); // Apply the change
            }
        }
    }
    
    /**
     * Example 2: Paged Menu
     * A menu with multiple pages of items.
     */
    public static class SimplePagedMenu extends PagedMenu {
        
        private final MiniMessage mm;
        
        public SimplePagedMenu(MiniMessage mm, List<String> playerNames) {
            super(54, mm.deserialize("<aqua>Player List"), 28); // 28 items per page
            this.mm = mm;
            
            // Create items for each player
            List<ItemStack> items = new ArrayList<>();
            for (String name : playerNames) {
                items.add(new ItemBuilder(Material.PLAYER_HEAD)
                        .name(String.format("<white>%s", name))
                        .build());
            }
            
            setItems(items);
            fillPageContent();
        }
        
        private void fillPageContent() {
            clear();
            
            // Fill borders (top row, bottom row, and sides)
            ItemStack border = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build();
            for (int i = 0; i < 54; i++) {
                if (i < 9 || i >= 45 || i % 9 == 0 || i % 9 == 8) {
                    setItem(i, border);
                }
            }
            
            // Add navigation buttons
            if (hasPreviousPage()) {
                setItem(45, new ItemBuilder(Material.ARROW).name("<yellow>Previous Page").build());
            }
            
            setItem(49, new ItemBuilder(Material.COMPASS)
                    .name("<gold>Page Info")
                    .lore(String.format("<gray>Page %d of %d", getCurrentPage() + 1, getTotalPages()))
                    .build());
            
            if (hasNextPage()) {
                setItem(53, new ItemBuilder(Material.ARROW).name("<yellow>Next Page").build());
            }
            
            // Add page items (slots 10-43 in a pattern, or just fill sequentially)
            List<ItemStack> pageItems = getCurrentPageItems();
            int slot = 10;
            for (ItemStack item : pageItems) {
                if (slot >= 44) break; // Don't overflow into navigation area
                setItem(slot++, item);
            }
            
            update(); // Apply all changes at once
        }
        
        @Override
        protected void onPageChange() {
            fillPageContent();
        }
        
        @Override
        public void handleClick(@NotNull InventoryClickEvent event) {
            int slot = event.getSlot();
            
            if (slot == 45 && hasPreviousPage()) {
                previousPage();
            } else if (slot == 53 && hasNextPage()) {
                nextPage();
            } else if (slot >= 10 && slot < 44) {
                // Handle clicking on an item
                ItemStack clicked = getItem(slot);
                if (clicked != null && event.getWhoClicked() instanceof Player player) {
                    player.sendMessage(Component.text("You clicked on an item!"));
                }
            }
        }
    }
    
    /**
     * Example 3: Opening a menu for a player
     */
    public static void openMenuExample(Player player, MiniMessage mm) {
        // Create a new menu
        SimpleMenu menu = new SimpleMenu(mm);
        
        // Open it for the player
        player.openInventory(menu.getInventory());
        
        // The MenuListener will automatically handle clicks
        // and prevent item theft by canceling the event
    }
    
    /**
     * Example 4: Deferred updates demonstration
     */
    public static class DeferredUpdateExample extends Menu {
        
        public DeferredUpdateExample(MiniMessage mm) {
            super(27, mm.deserialize("<green>Deferred Update Demo"));
            
            // Set 20 items - without buffering, these would flicker
            // as each item is set one by one
            for (int i = 0; i < 20; i++) {
                setItem(i, new ItemBuilder(Material.STONE)
                        .name(String.format("<gray>Item #%d", i + 1))
                        .build());
            }
            
            // All items appear at once when update() is called
            update();
            
            // You can also use refresh() - it's an alias for update()
            // refresh();
        }
        
        @Override
        public void handleClick(@NotNull InventoryClickEvent event) {
            // Handle clicks here
        }
    }
}
