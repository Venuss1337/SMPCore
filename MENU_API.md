# Menu/GUI API Documentation

This document describes the comprehensive Menu/GUI API for SMPCore plugin.

## Overview

The Menu API provides a robust framework for creating inventory-based GUIs in Minecraft plugins. It includes:

- **Deferred Updates**: Prevents visual flickering by buffering item changes
- **Memory Safety**: Uses `InventoryHolder` pattern instead of static Maps
- **Pagination Support**: Built-in support for multi-page menus
- **Event Handling**: Automatic event handling and theft prevention

## Architecture

### Core Classes

#### 1. `Menu` (Abstract Base Class)
The foundation of the API. Implements `InventoryHolder` and provides:
- Deferred update system with internal buffer
- Abstract `handleClick()` method for event handling
- `setItem()` - Buffers item changes
- `update()`/`refresh()` - Applies buffered changes to live inventory
- Memory-safe design relying on InventoryHolder link

#### 2. `PagedMenu` (Abstract Extension)
Extends `Menu` with pagination support:
- Multiple pages of items
- Navigation logic (next/previous page)
- Configurable items per page
- Abstract `onPageChange()` method for content refresh

#### 3. `MenuListener` (Event Handler)
Bukkit listener that:
- Listens for `InventoryClickEvent`
- Checks if `event.getInventory().getHolder()` is a `Menu` instance
- Cancels events to prevent item theft
- Delegates to the Menu's `handleClick()` method

## Usage Examples

### Example 1: Simple Menu

```java
import com.venuss.smpcore.menu.Menu;
import com.venuss.smpcore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SimpleShopMenu extends Menu {
    
    public SimpleShopMenu(MiniMessage mm) {
        super(27, mm.deserialize("<gold>Shop Menu"));
        
        // All setItem calls are buffered - no flickering!
        setItem(10, new ItemBuilder(Material.DIAMOND).name("<blue>Diamond - 100 coins").build());
        setItem(11, new ItemBuilder(Material.EMERALD).name("<green>Emerald - 50 coins").build());
        setItem(12, new ItemBuilder(Material.GOLD_INGOT).name("<yellow>Gold - 25 coins").build());
        
        // Apply all changes at once - prevents visual flickering
        update();
    }
    
    @Override
    public void handleClick(@NotNull InventoryClickEvent event) {
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();
        
        switch (slot) {
            case 10 -> player.sendMessage("You bought a diamond!");
            case 11 -> player.sendMessage("You bought an emerald!");
            case 12 -> player.sendMessage("You bought gold!");
        }
    }
}
```

### Example 2: Paged Menu

```java
import com.venuss.smpcore.menu.PagedMenu;

public class PlayerListMenu extends PagedMenu {
    
    public PlayerListMenu(MiniMessage mm, List<Player> players) {
        super(54, mm.deserialize("<aqua>Online Players"), 28);
        
        // Convert players to items
        List<ItemStack> items = new ArrayList<>();
        for (Player p : players) {
            items.add(new ItemBuilder(Material.PLAYER_HEAD)
                    .name(String.format("<white>%s", p.getName()))
                    .build());
        }
        
        setItems(items);
        refreshContent();
    }
    
    private void refreshContent() {
        clear();
        
        // Add navigation
        if (hasPreviousPage()) {
            setItem(47, new ItemBuilder(Material.ARROW).name("<yellow>Previous").build());
        }
        if (hasNextPage()) {
            setItem(51, new ItemBuilder(Material.ARROW).name("<yellow>Next").build());
        }
        
        // Add current page items
        List<ItemStack> pageItems = getCurrentPageItems();
        for (int i = 0; i < pageItems.size(); i++) {
            setItem(10 + i, pageItems.get(i));
        }
        
        update(); // Apply all at once
    }
    
    @Override
    protected void onPageChange() {
        refreshContent(); // Rebuild menu when page changes
    }
    
    @Override
    public void handleClick(@NotNull InventoryClickEvent event) {
        int slot = event.getSlot();
        
        if (slot == 47) {
            previousPage(); // Automatically calls onPageChange()
        } else if (slot == 51) {
            nextPage(); // Automatically calls onPageChange()
        }
    }
}
```

### Example 3: Opening a Menu

```java
public class MenuCommand {
    
    public void openShop(Player player, MiniMessage mm) {
        SimpleShopMenu menu = new SimpleShopMenu(mm);
        player.openInventory(menu.getInventory());
        
        // MenuListener automatically handles clicks and prevents theft
        // No need for manual event registration
    }
}
```

## Key Features

### 1. Deferred Updates (No Flickering)

The Menu API uses an internal buffer to prevent visual flickering:

```java
// Without deferred updates (OLD WAY - flickers):
for (int i = 0; i < 20; i++) {
    inventory.setItem(i, item); // Each item appears one by one - FLICKER!
}

// With deferred updates (NEW WAY - smooth):
for (int i = 0; i < 20; i++) {
    menu.setItem(i, item); // Buffered, not visible yet
}
menu.update(); // All items appear at once - NO FLICKER!
```

### 2. Memory Safety

Uses `InventoryHolder` pattern instead of static Maps:

```java
// BAD (Memory Leak Risk):
private static Map<Player, Menu> playerMenus = new HashMap<>();

// GOOD (Memory Safe):
public class Menu implements InventoryHolder {
    // The inventory itself holds reference to this Menu
    // When inventory is closed, Menu can be garbage collected
}
```

### 3. Automatic Event Handling

`MenuListener` handles all Menu instances automatically:

```java
@EventHandler
public void onInventoryClick(InventoryClickEvent event) {
    InventoryHolder holder = event.getClickedInventory().getHolder();
    
    if (holder instanceof Menu menu) {
        event.setCancelled(true); // Prevent item theft
        menu.handleClick(event);   // Delegate to menu
    }
}
```

## Integration

The Menu API is automatically integrated when the plugin loads:

```java
public class SMPCore extends JavaPlugin {
    @Override
    public void onEnable() {
        // Register MenuListener to handle all Menu instances
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }
}
```

## API Reference

### Menu Methods

| Method | Description |
|--------|-------------|
| `setItem(int slot, ItemStack item)` | Buffers an item change (not immediately visible) |
| `getItem(int slot)` | Gets the currently displayed item at slot |
| `update()` | Applies all buffered changes to live inventory |
| `refresh()` | Alias for `update()` |
| `clear()` | Clears all items (buffered operation) |
| `handleClick(InventoryClickEvent)` | Abstract method to handle click events |
| `getInventory()` | Returns the Bukkit Inventory instance |

### PagedMenu Methods

| Method | Description |
|--------|-------------|
| `setItems(List<ItemStack>)` | Sets all items to paginate |
| `addItem(ItemStack)` | Adds a single item to pagination |
| `getCurrentPageItems()` | Gets items for current page |
| `nextPage()` | Goes to next page, returns true if successful |
| `previousPage()` | Goes to previous page, returns true if successful |
| `goToPage(int page)` | Goes to specific page |
| `hasNextPage()` | Checks if next page exists |
| `hasPreviousPage()` | Checks if previous page exists |
| `getCurrentPage()` | Gets current page number (0-indexed) |
| `getTotalPages()` | Gets total number of pages |
| `onPageChange()` | Abstract method called when page changes |

## Best Practices

1. **Always call `update()` after batch operations**
   ```java
   for (int i = 0; i < 27; i++) {
       setItem(i, item);
   }
   update(); // Important!
   ```

2. **Use `onPageChange()` to rebuild content**
   ```java
   @Override
   protected void onPageChange() {
       rebuildMenu(); // Your method to repopulate items
   }
   ```

3. **Check for null in click handlers**
   ```java
   @Override
   public void handleClick(InventoryClickEvent event) {
       ItemStack clicked = getItem(event.getSlot());
       if (clicked == null) return;
       // Handle click...
   }
   ```

4. **Don't mix direct inventory access with buffered operations**
   ```java
   // BAD:
   setItem(10, item1);
   inventory.setItem(11, item2); // Bypasses buffer!
   update();
   
   // GOOD:
   setItem(10, item1);
   setItem(11, item2);
   update();
   ```

## Migration Guide

### Migrating Existing Menus

Old code:
```java
public class OldMenu implements InventoryHolder {
    private final Inventory inventory;
    
    public OldMenu() {
        this.inventory = Bukkit.createInventory(this, 27, Component.text("Menu"));
        inventory.setItem(0, item); // Direct access
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
```

New code:
```java
public class NewMenu extends Menu {
    
    public NewMenu(MiniMessage mm) {
        super(27, mm.deserialize("Menu"));
        setItem(0, item); // Buffered
        update(); // Apply
    }
    
    @Override
    public void handleClick(InventoryClickEvent event) {
        // Handle clicks
    }
}
```

## Requirements

- Java 21
- Paper API 1.21
- Bukkit/Spigot compatible server

## License

Part of SMPCore plugin by Venuss1337
