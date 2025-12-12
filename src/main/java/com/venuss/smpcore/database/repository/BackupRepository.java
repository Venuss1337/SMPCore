package com.venuss.smpcore.database.repository;

import com.venuss.smpcore.models.BackupInfo;
import com.venuss.smpcore.models.InventorySnapshot;
import com.venuss.smpcore.util.InventorySerializer;
import org.bukkit.entity.Player;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.venuss.smpcore.jooq.generated.Tables.PLAYER_INVENTORY_BACKUP;
import static com.venuss.smpcore.jooq.generated.Tables.USER;

public class BackupRepository {
    private final DSLContext context;

    public BackupRepository(DSLContext dsl) {
        this.context = dsl;
    }

    public void storeInventoryBackup(Player player, Optional<String> deathCause) {
        byte[] compressedInventory = InventorySerializer.toCompressedBytes(player.getInventory());

        context.transaction(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            UInteger userId = ctx.select(USER.USER_ID)
                    .from(USER)
                    .where(USER.UUID.eq(player.getUniqueId().toString()))
                    .fetchOne(USER.USER_ID, UInteger.class);

            ctx.insertInto(PLAYER_INVENTORY_BACKUP)
                    .set(PLAYER_INVENTORY_BACKUP.USER_ID, userId)
                    .set(PLAYER_INVENTORY_BACKUP.DEATH_CAUSE, deathCause.orElse(null))
                    .set(PLAYER_INVENTORY_BACKUP.INVENTORY_DATA, compressedInventory)
                    .execute();
        });
    }
    public List<BackupInfo> getInventoryBackupInfo(UUID uuid) {
        List<BackupInfo> backups = new ArrayList<>();
        context.transaction(configuration -> {
            DSLContext ctx = DSL.using(configuration);

            var result1 = ctx.select(USER.USER_ID, USER.NICKNAME)
                    .from(USER)
                    .where(USER.UUID.eq(uuid.toString()))
                    .fetchOne();

            if  (result1 == null) {
                return;
            }

            UInteger userId = result1.get(USER.USER_ID);
            String playerNickname = result1.get(USER.NICKNAME);

            var result2 = ctx.select(
                        PLAYER_INVENTORY_BACKUP.BACKUP_ID,
                        PLAYER_INVENTORY_BACKUP.DEATH_CAUSE,
                        PLAYER_INVENTORY_BACKUP.LAST_UPDATED
                    )
                    .from(PLAYER_INVENTORY_BACKUP)
                    .where(PLAYER_INVENTORY_BACKUP.USER_ID.eq(userId))
                    .fetch();
            for (var record : result2) {
                backups.add(
                        new BackupInfo(
                                record.get(PLAYER_INVENTORY_BACKUP.BACKUP_ID),
                                playerNickname,
                                record.get(PLAYER_INVENTORY_BACKUP.DEATH_CAUSE),
                                record.get(PLAYER_INVENTORY_BACKUP.LAST_UPDATED).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        )
                );
            }
        });
        return backups;
    }

    public InventorySnapshot getInventorySnapshot(UInteger backupId) {
        var inventoryData = context.select(PLAYER_INVENTORY_BACKUP.INVENTORY_DATA)
            .from(PLAYER_INVENTORY_BACKUP)
            .where(PLAYER_INVENTORY_BACKUP.BACKUP_ID.eq(backupId))
            .fetchOne(PLAYER_INVENTORY_BACKUP.INVENTORY_DATA);

        return new InventorySnapshot(inventoryData);
    }
}