package javagrinko.spring.tcp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.UUID;

public interface Connection {
    InetAddress getAddress();
    void send(byte[] bytes) throws IOException;
    void addListener(Listener listener);
    void start();
    void close() throws IOException;
    UUID getClientIdentifier();
    void setClientIdentifier(UUID newId);
    interface Listener {
        void messageReceived(Connection connection, byte[] bytes)
                throws InvocationTargetException, IllegalAccessException;

        void connected(Connection connection)
                throws InvocationTargetException, IllegalAccessException;

        void disconnected(Connection connection)
                throws InvocationTargetException, IllegalAccessException;
    }
}
