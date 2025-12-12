package com.venuss.smpcore.services;

import java.util.*;

public class ServiceManager {

    private Map<Class<? extends Service>, Service> services;

    public ServiceManager() {
        this.services = new HashMap<>();
    }

    public void startServices() {
        services.forEach((s, service) -> {
            service.start();
        });
    }

    public void restartServices() {
        services.forEach((s, service) -> {
            service.restart();
        });
    }

    public String getServiceStatus(Class<? extends Service> service) {
        return getService(service).status;
    }

    public Map<String, List<String>> getServicesStatus() {
        HashMap<String, List<String>> map = new HashMap<>();
        services.forEach((s, service) -> {
           map.put(service.serviceName, Collections.singletonList(service.status));
        });
        return map;
    }

    public void stopServices() {
        services.forEach((s, service) -> {
            service.stop();
        });
    }

    public <T extends Service> T getService(Class<T> serviceClass) {
        if (services.containsKey(serviceClass)) {
            return serviceClass.cast(services.get(serviceClass));
        }
        return null;
    }

    public boolean registerService(Class<? extends Service> serviceClass, Service service) {
        return services.put(serviceClass, service) != null;
    }

    public BackupService getBackupService() {
        return getService(BackupService.class);
    }
}
