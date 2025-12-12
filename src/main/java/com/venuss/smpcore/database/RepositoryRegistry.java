package com.venuss.smpcore.database;

import com.venuss.smpcore.database.repository.BackupRepository;
import com.venuss.smpcore.database.repository.UserRepository;
import org.jooq.DSLContext;

public class RepositoryRegistry {

    private static DSLContext dsl;

    private static BackupRepository BACKUP_REPO;
    private static UserRepository USER_REPO;
    //private static Map<Class<?>, Object> repositories = new HashMap<>();

    public static void init(DSLContext context) {
        dsl = context;
    }

//    public static <T> T get(Class<T> clazz) {
//        if (repositories.containsKey(clazz)) {
//            return (T) repositories.get(clazz);
//        }
//        return null;
//    }

    public static BackupRepository getBackupRepository() {
        if  (BACKUP_REPO == null) {
            return new BackupRepository(dsl);
        }
        return BACKUP_REPO;
    }
    public static UserRepository getUserRepository() {
        if  (USER_REPO == null) {
            return new UserRepository(dsl);
        }
        return USER_REPO;
    }
}
