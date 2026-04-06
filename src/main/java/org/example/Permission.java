package org.example;

public enum Permission {
    VIEW_STUDENTS(1),
    EDIT_STUDENTS(2),
    DELETE_STUDENTS(4),
    VIEW_REPORTS(8),
    SAVE_LOAD(16),
    MANAGE_USERS(32);

    private final int bit;

    Permission(int bit) {
        this.bit = bit;
    }

    public int getBit() {
        return bit;
    }
}
