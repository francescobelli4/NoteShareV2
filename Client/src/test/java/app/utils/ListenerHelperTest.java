package app.utils;

import app.views.GraphicController;
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

    private static class TestGraphicsController implements GraphicController {
        // Just for tests...
    }

    private static class TestView extends View<TestGraphicsController> implements TestModelListener {

        private int triggeredEventsCount = 0;

        public TestView() {
            super(new TestGraphicsController());
        }

        public int getTriggeredEventsCount() {
            return triggeredEventsCount;
        }

        @Override
        public void testEvent() {
            triggeredEventsCount++;
        }

        @Override
        protected void onClose() {
            // no-op
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