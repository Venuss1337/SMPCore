# Menu API Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                         Menu API Package                         │
│                  com.venuss.smpcore.menu                         │
└──────────────────────────────────────────────────────────────────┘
                               │
                               ├─────────────────────┐
                               │                     │
                               ▼                     ▼
                    ┌──────────────────┐  ┌──────────────────┐
                    │      Menu        │  │   MenuListener   │
                    │   (Abstract)     │  │   (Concrete)     │
                    ├──────────────────┤  ├──────────────────┤
                    │ - inventory      │  │ @EventHandler    │
                    │ - buffer         │  │ onInventoryClick │
                    │ - needsUpdate    │  └──────────────────┘
                    ├──────────────────┤            │
                    │ + setItem()      │            │ delegates to
                    │ + getItem()      │            └──────────────┐
                    │ + update()       │                           │
                    │ + refresh()      │                           │
                    │ + clear()        │                           │
                    │ # handleClick()  │◄──────────────────────────┘
                    └──────────────────┘
                            │
                            │ extends
                            ▼
                    ┌──────────────────┐
                    │   PagedMenu      │
                    │   (Abstract)     │
                    ├──────────────────┤
                    │ - currentPage    │
                    │ - totalPages     │
                    │ - items          │
                    │ - itemsPerPage   │
                    ├──────────────────┤
                    │ + setItems()     │
                    │ + addItem()      │
                    │ + nextPage()     │
                    │ + previousPage() │
                    │ + goToPage()     │
                    │ + hasNextPage()  │
                    │ # onPageChange() │
                    └──────────────────┘
                            │
                            │ extends
              ┌─────────────┴─────────────┐
              │                           │
              ▼                           ▼
      ┌──────────────┐          ┌──────────────────┐
      │  BackupMenu  │          │ BackupPlayerMenu │
      │  (Concrete)  │          │   (Concrete)     │
      └──────────────┘          └──────────────────┘

Key Design Principles:
═══════════════════════

1. DEFERRED UPDATES
   ┌─────────────────────────────────────────────────┐
   │ setItem() → buffer → update() → live inventory  │
   │                                                 │
   │ Prevents flickering by applying all changes     │
   │ at once instead of one-by-one                   │
   └─────────────────────────────────────────────────┘

2. MEMORY SAFETY
   ┌─────────────────────────────────────────────────┐
   │ Uses InventoryHolder pattern                    │
   │ No static Maps (Player → Menu)                  │
   │                                                 │
   │ Inventory → getHolder() → Menu                  │
   │ When inventory closes, Menu can be GC'd         │
   └─────────────────────────────────────────────────┘

3. EVENT DELEGATION
   ┌─────────────────────────────────────────────────┐
   │ MenuListener intercepts all InventoryClickEvent │
   │ Checks if holder instanceof Menu                │
   │ Cancels event (prevents theft)                  │
   │ Delegates to menu.handleClick()                 │
   └─────────────────────────────────────────────────┘

Usage Flow:
══════════

1. Create Menu Instance
   ┌──────────────────────────┐
   │ MyMenu extends Menu      │
   │   constructor:           │
   │     setItem(...)         │
   │     setItem(...)         │
   │     update()             │
   └──────────────────────────┘

2. Open for Player
   ┌──────────────────────────┐
   │ player.openInventory(    │
   │   menu.getInventory()    │
   │ )                        │
   └──────────────────────────┘

3. Player Clicks
   ┌──────────────────────────┐
   │ MenuListener             │
   │   - cancels event        │
   │   - calls handleClick()  │
   └──────────────────────────┘

4. Menu Updates
   ┌──────────────────────────┐
   │ handleClick() {          │
   │   setItem(...)           │
   │   update()               │
   │ }                        │
   └──────────────────────────┘
```
