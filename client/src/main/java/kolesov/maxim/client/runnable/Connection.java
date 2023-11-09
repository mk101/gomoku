package kolesov.maxim.client.runnable;

import com.fasterxml.jackson.databind.ObjectMapper;
import kolesov.maxim.client.config.ClientConfig;
import kolesov.maxim.common.dto.MessageAction;
import kolesov.maxim.common.dto.MessageDto;
import kolesov.maxim.common.dto.StateDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class Connection implements Runnable {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ClientConfig clientConfig;

    private final Consumer<StateDto> updateState;
    private final Consumer<Boolean> onWin;

    @Getter
    private UUID playerId;
    private DataOutputStream output;

    @Setter
    private boolean isClose = false;

    @Override
    public void run() {
        try (Socket socket = new Socket(clientConfig.getHost(), clientConfig.getPort())){
            log.info("Connected to {}:{}", socket.getInetAddress().getHostName(), socket.getPort());

            output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());

            MessageDto dto = OBJECT_MAPPER.readValue(input.readUTF(), MessageDto.class);
            if (dto.getAction() != MessageAction.CONNECTED) {
                return;
            }

            playerId = UUID.fromString(dto.getPayload().get("id").toString());

            while (!isClose) {
                if (input.available() == 0) {
                    continue;
                }

                proceedMessage(OBJECT_MAPPER.readValue(input.readUTF(), MessageDto.class));
            }

        } catch (Exception e) {
            log.error("Something went wrong", e);
        }

        log.info("Close connection");
    }

    @SneakyThrows
    public void sendMessage(MessageDto message) {
        output.writeUTF(OBJECT_MAPPER.writeValueAsString(message));
    }

    private void proceedMessage(MessageDto message) {
        switch (message.getAction()) {
            case STATE -> {
                StateDto state = OBJECT_MAPPER.convertValue(message.getPayload(), StateDto.class);
                updateState.accept(state);
            }
            case WIN -> {
                UUID id = UUID.fromString(message.getPayload().get("user").toString());
                if (id.equals(playerId)) {
                    log.info("You win");
                    onWin.accept(true);
                } else {
                    log.info("You loose");
                    onWin.accept(false);
                }
            }

            default -> log.error("Unexpected message");
        }
    }

}
