package app.views;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class should manage the user's navigation in the UI. Using a stack allows to keep the "navigation path" in
 * memory and go back to the previous page faster because pages get closed only when they get popped.
 * The display method should be implemented by Navigator subclasses.
 * @param <V>
 */
public abstract class Navigator<V extends View> {

    // This stack should keep trace of the user's navigation in the app
    private final Deque<V> stack = new ArrayDeque<>();

    public void push(V view) {
        stack.push(view);
        display(view);
    }

    public void pop() {
        if (stack.size() <= 1) return;

        stack.pop().close();
        display(stack.peek());
    }

    public void resetToPage(V view) {
        stack.forEach(View::close);
        stack.clear();
        push(view);
    }

    protected abstract void display(V view);
}
