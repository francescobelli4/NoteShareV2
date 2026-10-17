package app.views;

import java.util.ArrayDeque;
import java.util.Deque;

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

    protected void display(V view) {
        // To be implemented
    }
}
