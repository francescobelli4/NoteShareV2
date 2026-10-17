package app.views;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTest {

    private class TestView extends View {

        public TestView() {}
    }

    @Test
    void testClose() {

        TestView tv = new TestView();
        AtomicInteger unsubs = new AtomicInteger();

        tv.subscribe(unsubs::getAndIncrement);

        tv.close();

        assertEquals(1, unsubs.get());
    }

}