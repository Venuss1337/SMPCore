package com.venuss.smpcore.managers;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {

    public Map<Class<? extends Service>, Service> services = new HashMap<>();

    public <T extends Service> T getService(Class<T> managerClass) {
        if (services.containsKey(managerClass)) {
            return managerClass.cast(services.get(managerClass));
        }
        return null;
    }

    public BackupService getBackupService() {
        return (BackupService) getService(BackupService.class);
    }
}
