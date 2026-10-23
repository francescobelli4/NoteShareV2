package app.network;

import dtos.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class NetworkUserTest {

    @Test
    void testRead() throws IOException {

        try (MockedStatic<MessageHandler> mockedMessageHandler = Mockito.mockStatic(MessageHandler.class)) {

            LoginRequestDTO fakeRequest = new LoginRequestDTO(1, "username", "password");
            ServerSocket fakeServer = new ServerSocket(0);

            // Preparing a fake client connection in a different thread waiting to connect to a server
            Thread fakeClientThread = new Thread(() -> {
                try {
                    Socket clientSocket = new Socket("localhost", fakeServer.getLocalPort());

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(fakeRequest);
                    objectOutputStream.flush();
                    clientSocket.close();
                } catch (IOException _) {
                    // Ignore
                }
            });
            fakeClientThread.setDaemon(true);
            fakeClientThread.start();

            // Accepting the fake client on the fake server and running a NetworkUser to see if run() method in
            // NetworkUser calls MessageHandler.handleMessage
            Socket clientOnServerSocket = fakeServer.accept();
            NetworkUser networkUser = new NetworkUser(clientOnServerSocket);
            networkUser.run();
            mockedMessageHandler.verify(() -> MessageHandler.handleMessage(fakeRequest));
            assertTrue(clientOnServerSocket.isClosed());
        }
    }
}