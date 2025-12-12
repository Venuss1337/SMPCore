package com.venuss.smpcore;

import com.venuss.smpcore.commands.BackupCommand;
import com.venuss.smpcore.database.DatabaseRepository;
import com.venuss.smpcore.database.RepositoryRegistry;
import com.venuss.smpcore.exceptions.DEException;
import com.venuss.smpcore.listeners.PlayerJoinListener;
import com.venuss.smpcore.listeners.PlayerQuitListener;
import com.venuss.smpcore.services.BackupService;
import com.venuss.smpcore.services.ServiceManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMPCore extends JavaPlugin {

    private DatabaseRepository databaseRepository;
    public MiniMessage mm;

    @Override
    public void onEnable() {
        try {
            databaseRepository = new DatabaseRepository().initialize();
        } catch (DEException e) {
            getLogger().severe(e.getType().getMessage() + ", shutting down...");
            getServer().getPluginManager().disablePlugin(this);
        }

        this.mm = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.font())
                        .resolver(StandardTags.gradient())
                        .build()
                )
                .build();

        RepositoryRegistry.init(databaseRepository.getContext());

        ServiceManager serviceManager = new ServiceManager();
        serviceManager.registerService(BackupService.class, new BackupService(this));

        // minecraft shit
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(BackupCommand.createCommand(this, mm, serviceManager).build());
        });

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    @Override
    public void onDisable() {
        databaseRepository.shutdown();
    }
}
