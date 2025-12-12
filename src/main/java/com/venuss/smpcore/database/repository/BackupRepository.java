package com.venuss.smpcore.database.user;

import com.venuss.smpcore.models.InventorySnapshot;
import org.jooq.DSLContext;

import java.util.UUID;

public class BackupRepository {
    private DSLContext context;

    public BackupRepository(DSLContext dsl) {
        this.context = dsl;
    }

    public void storeBackup(InventorySnapshot inventorySnapshot, UUID uuid) {

    }
}
