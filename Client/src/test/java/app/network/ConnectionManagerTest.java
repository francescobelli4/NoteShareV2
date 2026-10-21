package app.network;

import dtos.LoginRequestDTO;
import dtos.LoginResponseDTO;
import dtos.Request;
import dtos.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {

    private ServerSocket testServer;

    /**
     * We should create a fake Server to connect
     * @throws IOException _
     * @throws EOFException _
     */
    @BeforeEach
    void setUp() throws IOException {

        testServer = new ServerSocket(0);

        Thread serverThread = new Thread(() -> {
            try (Socket clientSocket = testServer.accept();
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

                // Listening for requests from the client...
                while (!clientSocket.isClosed()) {

                    Request _ = (Request) in.readObject();
                    Response response = new LoginResponseDTO(2027, "pablito:D");

                    // Always answer with LoginResponseDTO for tests...
                    out.writeObject(response);
                    out.flush();
                }
            } catch (IOException | ClassNotFoundException _) {
                // ignore...
            }
        });
        serverThread.start();

        ConnectionManager.initializeConnection("localhost", testServer.getLocalPort());
    }


    @AfterEach
    void tearDown() throws IOException {
        // Resetting everything after each test for simplicity
        ConnectionManager.closeConnection();
        if (testServer != null && !testServer.isClosed()) {
            testServer.close();
        }
    }

    @Test
    void testSendSync() throws Exception {
        Request request = new LoginRequestDTO(2027, "pablito:D", "xXxXx");

        Response response = ConnectionManager.sendSync(request);

        assertEquals(2027, response.requestId());
    }

    @Test
    void testSendAsync() throws Exception {
        Request request = new LoginRequestDTO(2027, "pablito:D", "xXxXx");

        CompletableFuture<Response> future = ConnectionManager.sendAsync(request);

        // Waiting again like in Server/ConnectionManagerTest...
        Response response = future.get(2, TimeUnit.SECONDS);

        assertEquals(2027, response.requestId());
    }
}