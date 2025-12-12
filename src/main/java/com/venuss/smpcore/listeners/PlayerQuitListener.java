package com.venuss.smpcore.listeners;

import com.venuss.smpcore.services.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UserService.unloadUserfromMemory(event.getPlayer().getUniqueId(), true);
    }
}