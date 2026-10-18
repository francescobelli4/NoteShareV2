package app.views;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTest {

    private static class TestGraphicsController implements GraphicController {
        // Just for tests...
    }

    private static class TestView extends View<TestGraphicsController> {

        private int onCloseCalls = 0;
        TestView() {
            super(new TestGraphicsController());
        }

        void add(View<?> child) {
            addChild(child);
        }

        void remove(View<?> child) {
            removeChild(child);
        }

        public int getOnCloseCalls() {
            return onCloseCalls;
        }

        @Override
        protected void onClose() {
            onCloseCalls++;
        }
    }

    @Test
    void testClose() {

        TestView parentView = new TestView();
        TestView childView = new TestView();
        AtomicInteger unsubs = new AtomicInteger();

        parentView.subscribe(unsubs::getAndIncrement);
        parentView.add(childView);

        parentView.close();

        assertEquals(1, unsubs.get());
        assertEquals(1, parentView.getOnCloseCalls());
        assertEquals(1, childView.getOnCloseCalls());
    }

    @Test
    void testChildClose() {

        TestView parentView = new TestView();
        TestView childView = new TestView();

        parentView.add(childView);

        parentView.remove(childView);

        assertEquals(1, childView.getOnCloseCalls());
    }
}