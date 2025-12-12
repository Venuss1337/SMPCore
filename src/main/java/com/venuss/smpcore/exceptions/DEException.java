package com.venuss.smpcore.exceptions;

import org.jetbrains.annotations.NotNull;

public class DEException extends RuntimeException {
    DEExceptionType _type;

    public DEException(String message) {
        super(message);
    }

    public DEException(@NotNull DEExceptionType type) {
        super(type.toString());
        this._type = type;
    }

    public DEExceptionType getType() {
        return _type;
    }
}