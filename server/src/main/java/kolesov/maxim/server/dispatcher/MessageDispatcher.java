package kolesov.maxim.server.dispatcher;

import kolesov.maxim.common.dto.MessageDto;
import kolesov.maxim.server.model.socket.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageDispatcher {

    public void dispatch(Message message) {
        MessageDto payload = message.getPayload();
        switch (payload.getAction()) {

        }
    }

}
