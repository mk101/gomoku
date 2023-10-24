package kolesov.maxim.server.service.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import kolesov.maxim.common.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class SendService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final UserRegisterService userRegisterService;

    public void sendMessage(MessageDto dto) {
        for (UUID u : userRegisterService.getAllUserIds()) {
            sendMessage(u, dto);
        }
    }

    public void sendMessage(UUID userId, MessageDto dto) {
        Socket socket = userRegisterService.getUser(userId);
        if (socket == null) {
            throw new IllegalArgumentException(String.format("User with id %s not found", userId));
        }
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(OBJECT_MAPPER.writeValueAsString(dto));
            out.flush();
        } catch (Exception e) {
            log.error("Failed to send to user", e);
        }
    }

}
