package com.venuss.smpcore.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract extension of Menu that supports multiple pages.
 * Provides built-in navigation logic for next/previous page buttons.
 */
public abstract class PagedMenu extends Menu {
    
    protected int currentPage;
    protected int totalPages;
    protected final List<ItemStack> items;
    protected final int itemsPerPage;
    
    /**
     * Creates a paged menu with the specified parameters.
     * 
     * @param size The size of the inventory (must be multiple of 9, max 54)
     * @param title The title component for the inventory
     * @param itemsPerPage The number of items to display per page
     */
    protected PagedMenu(int size, Component title, int itemsPerPage) {
        super(size, title);
        this.currentPage = 0;
        this.items = new ArrayList<>();
        this.itemsPerPage = itemsPerPage;
        this.totalPages = 1;
    }
    
    /**
     * Sets the items to be displayed across pages.
     * This will recalculate the total number of pages.
     * 
     * @param items The list of items to paginate
     */
    public void setItems(@NotNull List<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
        this.totalPages = Math.max(1, (int) Math.ceil((double) items.size() / itemsPerPage));
        this.currentPage = Math.min(this.currentPage, this.totalPages - 1);
    }
    
    /**
     * Adds a single item to the paginated list.
     * This will recalculate the total number of pages.
     * 
     * @param item The item to add
     */
    public void addItem(@NotNull ItemStack item) {
        this.items.add(item);
        this.totalPages = Math.max(1, (int) Math.ceil((double) items.size() / itemsPerPage));
    }
    
    /**
     * Gets the items for the current page.
     * 
     * @return A list of items for the current page
     */
    @NotNull
    protected List<ItemStack> getCurrentPageItems() {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, items.size());
        
        if (startIndex >= items.size()) {
            return new ArrayList<>();
        }
        
        return new ArrayList<>(items.subList(startIndex, endIndex));
    }
    
    /**
     * Goes to the next page if available.
     * 
     * @return true if the page was changed, false if already on the last page
     */
    public boolean nextPage() {
        if (!hasNextPage()) {
            return false;
        }
        currentPage++;
        onPageChange();
        return true;
    }
    
    /**
     * Goes to the previous page if available.
     * 
     * @return true if the page was changed, false if already on the first page
     */
    public boolean previousPage() {
        if (!hasPreviousPage()) {
            return false;
        }
        currentPage--;
        onPageChange();
        return true;
    }
    
    /**
     * Goes to a specific page.
     * 
     * @param page The page number (0-indexed)
     * @return true if the page was changed, false if the page number is invalid
     */
    public boolean goToPage(int page) {
        if (page < 0 || page >= totalPages) {
            return false;
        }
        if (currentPage == page) {
            return false;
        }
        currentPage = page;
        onPageChange();
        return true;
    }
    
    /**
     * Checks if there is a next page available.
     * 
     * @return true if there is a next page
     */
    public boolean hasNextPage() {
        return currentPage < totalPages - 1;
    }
    
    /**
     * Checks if there is a previous page available.
     * 
     * @return true if there is a previous page
     */
    public boolean hasPreviousPage() {
        return currentPage > 0;
    }
    
    /**
     * Gets the current page number (0-indexed).
     * 
     * @return The current page number
     */
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * Gets the total number of pages.
     * 
     * @return The total number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }
    
    /**
     * Called when the page changes.
     * Subclasses should override this to repopulate the menu with new page content.
     */
    protected abstract void onPageChange();
}
