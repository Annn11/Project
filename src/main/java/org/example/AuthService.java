package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthService {
    private final Map<String, UserAccount> users = new HashMap<>();
    private UserAccount currentUser;

    public AuthService() {
        users.put("user", new UserAccount("user", "1234", UserRole.USER));
        users.put("manager", new UserAccount("manager", "admin", UserRole.MANAGER));
        users.put("admin", new UserAccount("admin", "root", UserRole.ADMIN));
    }

    public boolean login(String login, String password) {
        UserAccount user = users.get(login);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }


    public UserAccount getCurrentUser() {
        return currentUser;
    }

    public UserRole getCurrentRole() {
        return currentUser == null ? null : currentUser.getRole();
    }

    public boolean hasPermission(Permission permission) {
        if (currentUser == null) {
            return false;
        }
        return AccessManager.hasPermission(currentUser.getRole(), permission);
    }

    public boolean addUser(String login, String password, UserRole role) {
        if (login == null || login.isBlank() || password == null || password.isBlank() || role == null) {
            return false;
        }
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new UserAccount(login, password, role));
        return true;
    }

    public boolean removeUser(String login) {
        if (login == null || login.isBlank()) {
            return false;
        }
        if ("admin".equals(login)) {
            return false;
        }
        return users.remove(login) != null;
    }

    public boolean changeRole(String login, UserRole newRole) {
        UserAccount user = users.get(login);
        if (user == null || newRole == null) {
            return false;
        }
        user.setRole(newRole);
        return true;
    }

    public List<UserAccount> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}