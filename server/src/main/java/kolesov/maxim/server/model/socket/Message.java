package kolesov.maxim.server.model.socket;

import kolesov.maxim.common.dto.MessageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
public class Message {

    private Header header;
    private MessageDto payload;

    public Message(UUID userId, MessageDto dto, MessageDirection direction) {
        this.header = new Header(userId, direction);
        this.payload = dto;
    }

    public enum MessageDirection {
        IN, OUT
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Header {

        private UUID userId; // null -> broadcast
        private MessageDirection direction;

    }

}
