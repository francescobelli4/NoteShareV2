package app.network;

import java.io.*;

/**
 * This class was added to fix a security issue on SonarCloud. Using the default ObjectInputStream allows an
 * attacker to craft a malicious object that server can de-serialize and use. To prevent this, we want this class
 * to only resolve classes we whitelisted.
 */
public class SafeObjectInputStream extends ObjectInputStream {
    public SafeObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    /**
     * This function resolves the local class equivalent of every class description received from the stream.
     * For example, when a LoginRequestDTO arrives, it's called to resolve dtos.LoginRequestDTO and every non-primitive
     * internal variable.
     * To solve the security issue, we should whitelist the classes we want to be able to de-serialize.
     * @param desc an instance of class {@code ObjectStreamClass}
     * @return a whitelisted Class<?>
     * @throws IOException any of the usual Input/Output exceptions
     * @throws ClassNotFoundException if class of a serialized object cannot be found
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Class<?> defaultResolved = super.resolveClass(desc);

        // SonarCloud suggests to throw this exception
        if (!isWhitelisted(defaultResolved)) {
            throw new InvalidClassException("Unauthorized deserialization", defaultResolved.getName());
        }

        return defaultResolved;
    }

    /**
     * This class should accept the whitelisted class types.
     * <a href="https://www.tutorialspoint.com/article/get-the-fully-qualified-name-of-a-class-in-java">This page</a>
     * explains how to get a Class full name.
     * @param defaultResolved the Class<?> accepted from the default resolveClass method.
     * @return true if this class is whitelisted
     */
    private boolean isWhitelisted(Class<?> defaultResolved) {

        // In case the resolved class is an array of arrays, we should reach the arrays type
        while (defaultResolved.isArray()) {
            defaultResolved = defaultResolved.getComponentType();
        }

        // At this point, we want to accept a class type matching this condition
        return defaultResolved.isPrimitive() ||                         // The type is a primitive type
                defaultResolved.getName().startsWith("dtos.") ||        // The type is a DTO I created
                defaultResolved.getName().startsWith("java.util.") ||   // The type is a collection like ArrayList
                defaultResolved.getName().startsWith("java.lang.") ||   // writeObject transforms primitives in Objects
                defaultResolved.getName().startsWith("permissions.") || // The type is a Permission or Role enum
                defaultResolved.isEnum();                          // The type is an Enum
    }
}
