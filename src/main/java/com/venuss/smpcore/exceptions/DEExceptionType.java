package com.venuss.smpcore.exceptions;

public enum DEExceptionType {
    FAILED_TO_CREATE_TABLES("Failed to connect to database"),
    FAILED_TO_LOAD_DATA("Migrations not found"),
    MIGRATIONS_NOT_FOUND("Failed to create tables"),
    FAILED_TO_CONNECT("Failed to connect to database"),
    UNEXPECTED_EXCEPTION("Unknown error occurred");

    private String message;

    DEExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
