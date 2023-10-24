package kolesov.maxim.server.service.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class UserRegisterService {

    private final Map<UUID, Socket> sockets = new ConcurrentHashMap<>();

    public UUID registerUser(Socket socket) {
        UUID id = UUID.randomUUID();
        sockets.put(id, socket);

        return id;
    }

    public void deleteUser(UUID id) {
        Socket socket = sockets.remove(id);
        if (socket == null) {
            throw new IllegalArgumentException(String.format("User with id %s not found", id));
        }

        try {
            socket.close();
        } catch (IOException e) {
            log.error("Failed to close socket", e);
        }
    }

    public Socket getUser(UUID id) {
        Socket user = sockets.get(id);

        if (user == null) {
            throw new IllegalArgumentException(String.format("User with id %s not found", id));
        }

        return user;
    }

    public Set<UUID> getAllUserIds() {
        return sockets.keySet();
    }

}
