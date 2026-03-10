package org.example;

public class UserAccount {

    private final String login;
    private final String password;
    private final UserRole role;

    public UserAccount(String login, String password, UserRole role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}