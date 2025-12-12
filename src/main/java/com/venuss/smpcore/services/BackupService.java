package com.venuss.smpcore.services;

import com.venuss.smpcore.SMPCore;
import com.venuss.smpcore.database.RepositoryRegistry;
import com.venuss.smpcore.database.repository.BackupRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class BackupService extends Service {

    public BackupService(SMPCore plugin) {
        super("BackupService");

        plugin.getServer().getPluginManager().registerEvents(new BackupListener(plugin.mm), plugin);
    }

    public BackupRepository getBackupRepository() {
        return RepositoryRegistry.getBackupRepository();
    }

    class BackupListener implements Listener {
        MiniMessage mm;

        BackupListener(MiniMessage mm) {
            this.mm = mm;
        }

        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent event) {
            var cl = event.deathMessage();
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            assert cl != null;
            var something = serializer.serialize(cl);


            System.out.println(something);

            RepositoryRegistry.getBackupRepository().storeInventoryBackup(
                    event.getPlayer().getPlayer(), Optional.of(something));
        }
    }


}
