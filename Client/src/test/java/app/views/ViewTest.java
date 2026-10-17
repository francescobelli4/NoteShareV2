package app.views;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTest {

    private static class TestView extends View {

        private int onCloseCalls = 0;
        public TestView() {
            onCloseCalls = 0;
        }

        void add(View child) {
            addChild(child);
        }

        void remove(View child) {
            removeChild(child);
        }

        public int getOnCloseCalls() {
            return onCloseCalls;
        }

        @Override
        protected void onClose() {
            onCloseCalls++;
            super.onClose();
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

}