package org.example;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, UserAccount> users = new HashMap<>();
    private UserAccount currentUser;

    public AuthService() {
        users.put("user", new UserAccount("user", "1234", UserRole.USER));
        users.put("manager", new UserAccount("manager", "admin", UserRole.MANAGER));
    }

    public boolean login(String login, String password) {
        UserAccount user = users.get(login);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean isManager() {
        return currentUser != null && currentUser.getRole() == UserRole.MANAGER;
    }

    public UserRole getCurrentRole() {
        return currentUser == null ? null : currentUser.getRole();
    }
}
