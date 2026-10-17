package app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class should provide to the models a way to know who is listening to their events.
 * For example, if a hypothetical model has an event "onUpdate", a ListenerHelper object like:
 *      private ListenerHelper<ThisModelListener> listeners = ...;
 * should be created in the model's class. So, ThisModelListener will be an interface class like:
 *      public interface ThisModelListener {
 *          void onUpdate();
 *      }
 * at this point, when the model has to fire the event, it should just use:
 *      listeners.fire(l -> l.onUpdate());
 * @param <L>
 */
public class ListenerHelper<L> {
    private final List<L> listeners = new ArrayList<>();

    /**
     * This function should add a new listener to the list and return a Subscription object to him: in this way, the
     * listener can cancel the subscription when it's no longer needed.
     * @param listener the new listener
     * @return a reference to the subscription
     */
    public Subscription subscribe(L listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    /**
     * This function should fire an event on every listener.
     * @param event a lambda function mainly
     */
    public void triggerEvent(Consumer<L> event) {
        listeners.forEach(event);
    }

    public int getListenersCount() {
        return listeners.size();
    }
}
