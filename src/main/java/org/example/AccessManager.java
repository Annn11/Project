package org.example;

public class AccessManager {

    public static int getPermissionsMask(UserRole role) {
        return switch (role) {
            case USER ->
                    Permission.VIEW_STUDENTS.getBit()
                            | Permission.VIEW_REPORTS.getBit();

            case MANAGER ->
                    Permission.VIEW_STUDENTS.getBit()
                            | Permission.EDIT_STUDENTS.getBit()
                            | Permission.DELETE_STUDENTS.getBit()
                            | Permission.VIEW_REPORTS.getBit()
                            | Permission.SAVE_LOAD.getBit();

            case ADMIN ->
                    Permission.VIEW_STUDENTS.getBit()
                            | Permission.EDIT_STUDENTS.getBit()
                            | Permission.DELETE_STUDENTS.getBit()
                            | Permission.VIEW_REPORTS.getBit()
                            | Permission.SAVE_LOAD.getBit()
                            | Permission.MANAGE_USERS.getBit();
        };
    }

    public static boolean hasPermission(UserRole role, Permission permission) {
        int mask = getPermissionsMask(role);
        return (mask & permission.getBit()) != 0;
    }
}