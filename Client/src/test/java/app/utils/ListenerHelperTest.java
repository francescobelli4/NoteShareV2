package app.utils;

import app.views.View;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListenerHelperTest {

    private static class TestModel {

        private final ListenerHelper<TestModelListener> listeners;

        public TestModel() {
            listeners = new ListenerHelper<>();
        }

        public Subscription addListener(TestModelListener listener) {
            return listeners.subscribe(listener);
        }

        public void event() {
            listeners.triggerEvent(TestModelListener::testEvent);
        }


    }

    private interface TestModelListener {
        void testEvent();
    }

    private static class TestView extends View implements TestModelListener {

        private int triggeredEventsCount;

        public TestView() {
            triggeredEventsCount = 0;
        }

        public int getTriggeredEventsCount() {
            return triggeredEventsCount;
        }

        @Override
        public void testEvent() {
            triggeredEventsCount++;
        }
    }

    @Test
    void subscribe() {

        TestModel model = new TestModel();
        TestView view = new TestView();

        Subscription _ = model.addListener(view);

        assertEquals(1, model.listeners.getListenersCount());
    }

    @Test
    void triggerEvent() {

        TestModel model = new TestModel();
        TestView view = new TestView();

        Subscription _ = model.addListener(view);

        model.event();
        assertEquals(1, view.getTriggeredEventsCount());
    }
}