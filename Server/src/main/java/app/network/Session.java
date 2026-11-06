package app.network;

import permissions.Permission;
import permissions.Role;

public class Session {

    private String username;
    private Role role;
    private boolean loggedIn = false;

    public void logIn(String username, Role role) {
        this.username = username;
        this.role = role;
        loggedIn = true;
    }

    public boolean hasPermission(Permission permission) {
        return role != null && role.hasPermission(permission);
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
