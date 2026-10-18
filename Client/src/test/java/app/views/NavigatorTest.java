package app.views;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigatorTest {

    private static class TestView extends View {

        private int onCloseCalls = 0;
        public TestView() {
            onCloseCalls = 0;
        }

        public int getOnCloseCalls() {
            return onCloseCalls;
        }

        @Override
        protected void onClose() {
            onCloseCalls++;
        }
    }

    private int displayCount = 0;

    Navigator<TestView> navigator = new Navigator<TestView>() {
        @Override
        protected void display(TestView view) {
            displayCount++;
        }
    };


    @Test
    void push() {
        TestView view = new TestView();

        navigator.push(view);

        assertAll(() -> {
           assertEquals(1, displayCount);
           assertEquals(1, navigator.getStackSize());
        });
    }

    @Test
    void pop() {
        TestView view = new TestView();
        TestView secondView = new TestView();

        navigator.push(view);
        navigator.pop();

        assertAll(() -> {
            assertEquals(0, view.getOnCloseCalls());
            assertEquals(1, navigator.getStackSize());
        });

        navigator.push(secondView);
        navigator.pop();

        assertAll(() -> {
            assertEquals(1, secondView.getOnCloseCalls());
            assertEquals(1, navigator.getStackSize());
        });
    }

    @Test
    void resetToPage() {

        TestView view = new TestView();
        TestView secondView = new TestView();

        navigator.push(view);

        navigator.resetToPage(secondView);

        assertAll(() -> {
            assertEquals(1, view.getOnCloseCalls());
            assertEquals(1, navigator.getStackSize());
            assertEquals(2, displayCount);
        });
    }
}