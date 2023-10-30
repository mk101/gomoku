package kolesov.maxim.server.runnable;

import com.fasterxml.jackson.databind.ObjectMapper;
import kolesov.maxim.common.dto.MessageDto;
import kolesov.maxim.server.config.ServerStateConfig;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.socket.UserRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import static kolesov.maxim.server.model.socket.Message.MessageDirection.*;

@Slf4j
@RequiredArgsConstructor
public class ClientHandler implements Runnable {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Socket socket;
    private final ServerStateConfig serverStateConfig;
    private final BlockingQueue<Message> messageQueue;
    private final UserRegisterService userRegisterService;

    @Override
    @SneakyThrows
    public void run() {
        log.info("Connected from {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
        DataInputStream input = new DataInputStream(socket.getInputStream());

        UUID userId = userRegisterService.registerUser(socket);

        while (!serverStateConfig.isShutdown()) {

            if (input.available() > 0) {
                MessageDto dto = OBJECT_MAPPER.readValue(input.readUTF(), MessageDto.class);
                messageQueue.put(new Message(userId, dto, IN));
            }

        }

        userRegisterService.deleteUser(userId);
    }

}
