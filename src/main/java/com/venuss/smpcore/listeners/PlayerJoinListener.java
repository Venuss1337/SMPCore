package com.venuss.smpcore.listeners;

import com.venuss.smpcore.services.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        UserService.loadUserIntoMemory(event.getUniqueId(), event.getPlayerProfile().getName());
    }
}