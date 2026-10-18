package app.views;

import app.utils.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * This class should be the most basic implementation of a view. Every view can be closed, add or remove child
 * views, subscribe as a listener to models.
 */
public abstract class View<C extends GraphicController> {

    protected C controller;

    protected View(C controller) {
        this.controller = controller;
    }

    // A list of this view's subscriptions
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final List<View<?>> children = new ArrayList<>();

    protected void subscribe(Subscription s) {
        subscriptions.add(s);
    }

    protected final void addChild(View<?> child) {
        children.add(child);
    }

    protected final void removeChild(View<?> child) {
        children.remove(child);
        child.close();
    }

    public final void close() {
        children.forEach(View::close);
        children.clear();
        // Cancel every view's subscription
        subscriptions.forEach((Subscription::cancel));
        subscriptions.clear();

        onClose();
    }

    protected abstract void onClose();
}
