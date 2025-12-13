package com.venuss.smpcore.services;

import com.venuss.smpcore.SMPCore;
import com.venuss.smpcore.database.RepositoryRegistry;
import com.venuss.smpcore.database.repository.BackupRepository;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
        private MiniMessage mm;

        BackupListener(MiniMessage mm) {
            this.mm = mm;
        }

        @EventHandler
        public void onPlayerDeath(PlayerDeathEvent event) {
            var deathMessage = event.deathMessage();
            assert deathMessage != null;

            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

            RepositoryRegistry.getBackupRepository().storeInventoryBackup(
                    event.getPlayer(), Optional.of(serializer.serialize(deathMessage))
            );
        }
    }


}
