package com.venuss.smpcore.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.venuss.smpcore.inventory.BackupMenu;
import com.venuss.smpcore.services.ServiceManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public class BackupCommand  {

    public static LiteralArgumentBuilder<CommandSourceStack> createCommand(Plugin plugin, MiniMessage mm, ServiceManager serviceManager) {
        return Commands.literal("backup")
                .then(Commands.argument("target", ArgumentTypes.player())
                        // checking required permission
                        .requires(source -> source.getSender().hasPermission("smpcore.essentials.backup"))
                        // checking if the sender is player and not console
                        // not sure how to add custom message so disabled for now
                        // .requires(source -> source.getSender() instanceof Player)
                        // suggesting only online players for the first argument
                        .suggests((context, builder) -> {
                            Bukkit.getOnlinePlayers().stream()
                                    .map(Player::getName)
                                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                                    .forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .executes(context -> playerBackupMenu(context, mm, serviceManager))
                );
    }

    private static int playerBackupMenu(CommandContext<CommandSourceStack> context, MiniMessage mm, ServiceManager serviceManger) throws CommandSyntaxException {
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
        final Player target = targetResolver.resolve(context.getSource()).getFirst();

        CommandSender sender = context.getSource().getSender();
        if (!(sender instanceof Player player)) {
            sender.sendPlainMessage("Command only for players!");
            return Command.SINGLE_SUCCESS;
        }

        BackupMenu backupMenu = new BackupMenu(
                mm,
                serviceManger.getBackupService().getBackupRepository().getInventoryBackupInfo(target.getUniqueId())
        );
        player.openInventory(backupMenu.getInventory());
        return Command.SINGLE_SUCCESS;
    }
}
