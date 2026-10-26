package permissions;

import java.util.EnumSet;
import java.util.Set;

public enum Role {
    STUDENT(EnumSet.of(Permission.BUY_NOTES, Permission.SELL_NOTES, Permission.BROWSE_LISTINGS, Permission.JOIN_CLASSES,
            Permission.UPLOAD_NOTES, Permission.REPORT_NOTES)),
    TEACHER(EnumSet.of(Permission.CREATE_CLASSES, Permission.UPLOAD_NOTES_IN_CLASSES)),
    MODERATOR(EnumSet.of(Permission.VIEW_REPORTED_NOTES, Permission.VIEW_ALL_NOTES, Permission.DELETE_NOTES));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
