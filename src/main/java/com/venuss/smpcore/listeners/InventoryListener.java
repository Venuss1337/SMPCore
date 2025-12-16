package com.venuss.smpcore.listeners;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.Listener;

/**
 * Legacy inventory listener - now mostly handled by MenuListener.
 * This can be used for non-Menu inventory interactions if needed.
 */
public class InventoryListener implements Listener {

    private static MiniMessage mm;

    public InventoryListener(MiniMessage miniMessageInstance) {
        mm = miniMessageInstance;
    }

    // Add custom inventory event handlers here that don't use the Menu API
}
