package com.venuss.smpcore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack is;
    private static MiniMessage mm;

    public static void init(MiniMessage miniMessage) {
        mm = miniMessage;
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }
    public ItemBuilder(Material m) {
        this.is = new ItemStack(m);
    }
    public ItemBuilder(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }
    public ItemBuilder(Material m, int amount, short durability) {
        this.is = new ItemStack(m, amount, durability);
    }
    public ItemBuilder name(String name) {
        this.is.editMeta(meta -> meta.displayName(mm.deserialize(name)));
        return this;
    }
    public ItemBuilder lore(String... lore) {
        this.is.editMeta(meta -> meta.lore(List.of(mm.deserialize(Arrays.toString(lore)))));
        return this;
    }
    public ItemBuilder lore(Component... lore) {
        this.is.editMeta(meta -> meta.lore(List.of(lore)));
        return this;
    }
    public ItemStack build() {
        return this.is;
    }
}
