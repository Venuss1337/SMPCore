package com.venuss.smpcore.models;

import org.jooq.types.UInteger;

public record BackupInfo(UInteger backupId, String nickname, String deathCause, String dateCreated) {

}