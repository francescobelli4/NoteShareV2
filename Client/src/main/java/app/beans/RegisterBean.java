package app.beans;

import permissions.Role;

public record RegisterBean(String username, String password, Role role) { }
