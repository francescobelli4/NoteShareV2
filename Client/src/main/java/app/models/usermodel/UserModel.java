package app.models.usermodel;

import app.utils.ListenerHelper;
import app.utils.Subscription;
import permissions.Permission;
import permissions.Role;

/**
 * This model represents a user in the app. The listeners subscription system that I built before guarantees that
 * every listener receives the events specified in UserModelListener interface and also every listener can remove
 * its own subscription.
 * The app should manage 3 types of users:
 * - STUDENT:   a student can buy and sell notes, subscribe to teacher's classes, load his notes. So, a student should
 *              have coins.
 *
 * - TEACHER:   a teacher can create classes and share notes in their classes. They should not have coins.
 *
 * - MODERATOR: a moderator should be able to access all the notes in the marketplace to check or delete them. They
 *              should not have coins.
 *
 * So, it's a good idea to associate a Role to the UserModel and every Role is linked to its granted permissions.
 * This solution needs the app controllers to check the user's role and permissions before executing use cases.
 * Also, a user type could need additional data (like coins number) that the other types do not need. The solution that
 * I found is to use additional models (for example, I'll add a WalletModel for coins) that are not directly linked
 * to the UserModel, so they are used only when necessary.
 */
public class UserModel {

    private final ListenerHelper<UserModelListener> listeners = new ListenerHelper<>();
    private String username;
    private Role role;
    private boolean loggedIn = false;

    public Subscription addListener(UserModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void logIn(String username, Role role) {
        this.username = username;
        this.role = role;

        loggedIn = true;
        listeners.triggerEvent(UserModelListener::onLoggedIn);
    }

    public void logOut() {
        this.username = null;
        this.role = null;

        loggedIn = false;
        listeners.triggerEvent(UserModelListener::onLoggedOut);
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
