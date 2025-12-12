package com.venuss.smpcore.database.repository;

import com.venuss.smpcore.models.User;
import org.jooq.DSLContext;

import java.util.UUID;

import static com.venuss.smpcore.jooq.generated.Tables.USER;

public class UserRepository {

    private DSLContext context;

    public UserRepository(DSLContext dsl) {
        this.context = dsl;
    }

    public User getUser(UUID uuid) {
        var r = context
                .select(USER.UUID, USER.NICKNAME)
                .from(USER)
                .where(USER.UUID.eq(uuid.toString()))
                .fetchOne();
        if  (r == null) {
            return null;
        }
        return new User(
                UUID.fromString(r.get(USER.UUID)),
                r.get(USER.NICKNAME)
        );
    }
    public boolean storeUser(UUID uuid, String nickname) {
        var r = context
                .insertInto(USER)
                .set(USER.UUID, uuid.toString())
                .set(USER.NICKNAME, nickname)
                .execute();
        return r > 0;
    }
    public boolean deleteUser(UUID uuid) {
        var r = context
                .deleteFrom(USER)
                .where(USER.UUID.eq(uuid.toString()))
                .execute();
        return r > 0;
    }
    public boolean userExists(UUID uuid) {
        var r = context
                .selectCount()
                .from(USER)
                .where(USER.UUID.eq(uuid.toString()))
                .fetchOne(0,  int.class);
        if (r == null) {
            return false;
        }
        return r == 1;
    }

    public User updateUser(UUID uuid, String nickname) {
        var r = context
                .update(USER)
                .set(USER.NICKNAME, nickname)
                .where(USER.UUID.eq(uuid.toString()))
                .returningResult(USER.NICKNAME)
                .fetchOneInto(String.class);

        return new User(
                uuid,
                nickname
        );
    }
}
