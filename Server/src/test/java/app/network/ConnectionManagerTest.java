package app.network;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class ConnectionManagerTest {

    @Test
    void initializeServerSocket() throws IOException {

        AtomicInteger acceptedCount = new AtomicInteger();
        ConnectionManager connectionManager = new ConnectionManager(0, socket -> acceptedCount.getAndIncrement());

        connectionManager.initializeServerSocket();

        // Using a different thread to avoid blocking this one
        Thread t = new Thread(connectionManager::acceptClients);
        t.start();

        try (var _ = new Socket("localhost", connectionManager.getPort())) {
            // Wait for the callback :D
            assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
                while (acceptedCount.get() < 1) {
                    Thread.onSpinWait();
                }
            });
            assertEquals(1, acceptedCount.get());
        }

        connectionManager.closeServerSocket();
        t.interrupt();
    }
}