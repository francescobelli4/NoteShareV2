package app.network;

import app.config.Mode;
import dtos.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import permissions.Role;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SafeObjectInputStreamTest {

    @Test
    void resolveSuccess() throws IOException, ClassNotFoundException {

        // Using a ByteArrayOutputStream to simulate a stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        // Writing allowed objects in the stream
        // defaultResolved.getName().startsWith("dtos.")
        out.writeObject(new LoginRequestDTO(1, "username", "password"));
        // defaultResolved.isPrimitive() || defaultResolved.getName().startsWith("java.lang.")
        int n = 2026;
        out.writeObject(n);
        // defaultResolved.getName().startsWith("java.util.")
        out.writeObject(new ArrayList<>());
        // defaultResolved.isEnum
        out.writeObject(Mode.DEMO);
        // Arrays
        out.writeObject(new String[] {"test1", "test2"});
        // Multi-dim arrays
        out.writeObject(new int[3][2]);
        // Role or Permission
        out.writeObject(Role.STUDENT);

        // We should now create a SafeObjectInputStream that receives the output stream we created above
        SafeObjectInputStream safeObjectInputStream = new SafeObjectInputStream(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        );
        // It should now properly read all the objects
        assertNotNull(safeObjectInputStream.readObject());
        assertNotNull(safeObjectInputStream.readObject());
        assertNotNull(safeObjectInputStream.readObject());
        assertNotNull(safeObjectInputStream.readObject());
        assertNotNull(safeObjectInputStream.readObject());
        assertNotNull(safeObjectInputStream.readObject());
        assertNotNull(safeObjectInputStream.readObject());
    }

    @Test
    void resolveFail() throws IOException {

        // Using a ByteArrayOutputStream to simulate a stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        // Not in whitelist
        out.writeObject(new File("not_whitelisted.txt"));

        // We should now create a SafeObjectInputStream that receives the output stream we created above
        SafeObjectInputStream safeObjectInputStream = new SafeObjectInputStream(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        );

        assertThrows(InvalidClassException.class, safeObjectInputStream::readObject);
    }
}