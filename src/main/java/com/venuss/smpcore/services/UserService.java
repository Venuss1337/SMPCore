package com.venuss.smpcore.services;

import com.venuss.smpcore.database.RepositoryRegistry;
import com.venuss.smpcore.models.User;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserService extends Service {

    private static final ConcurrentHashMap<UUID, User> ONLINE_USERS = new ConcurrentHashMap<>();

    public UserService() {
        super("UserService");
    }

    public static Optional<User> getUser(UUID uuid) {
        if  (ONLINE_USERS.containsKey(uuid)) {
            return Optional.of(ONLINE_USERS.get(uuid));
        }

        User user = RepositoryRegistry.getUserRepository().getUser(uuid);
        return Optional.ofNullable(user);
    }

    public static void loadUserIntoMemory(UUID uuid, String nickname) {
        User user = RepositoryRegistry.getUserRepository().getUser(uuid);
        if (user == null) {
            user = new User(uuid, nickname);
            RepositoryRegistry.getUserRepository().storeUser(uuid, nickname);
        }

        user.setOnline(true);
        ONLINE_USERS.put(uuid, user);
    }

    public static void unloadUserfromMemory(UUID uuid, boolean saveChanges) {
        var user = ONLINE_USERS.get(uuid);
        if (user == null) { return; }

        user.setOnline(false);
        ONLINE_USERS.remove(uuid);
        if (saveChanges) {
            // WARNING: WITH MORE INFO THIS WILL CHANGE
            RepositoryRegistry.getUserRepository().updateUser(uuid, user.getNickname());
        }
    }

    public static boolean isOnline(UUID uuid) {
        return ONLINE_USERS.containsKey(uuid);
    }

    public static void saveChanges(UUID uuid) {
        var user =  ONLINE_USERS.get(uuid);
        if (user == null) { return; }

        // WARNING: WITH MORE INFO THIS WILL CHANGE
        RepositoryRegistry.getUserRepository().updateUser(uuid, user.getNickname());
    }

    public static @NotNull Collection<User> getAllUsers() {
        return ONLINE_USERS.values();
    }
}
