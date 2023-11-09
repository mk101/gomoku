package kolesov.maxim.server.runnable;

import kolesov.maxim.server.model.socket.ServerState;
import kolesov.maxim.server.dispatcher.MessageDispatcher;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.socket.SendService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class MessageListener implements Runnable {

    private final BlockingQueue<Message> messageQueue;
    private final ServerState serverState;
    private final SendService sendService;
    private final MessageDispatcher messageDispatcher;

    @Override
    @SneakyThrows
    public void run() {
        while (!serverState.isShutdown()) {
            Message message = messageQueue.take();
            if (message.getHeader().getDirection() == Message.MessageDirection.OUT) {
                if (message.getHeader().getUserId() == null) {
                    sendService.sendMessage(message.getPayload());
                    continue;
                }

                sendService.sendMessage(message.getHeader().getUserId(), message.getPayload());
                continue;
            }

            try {
                messageDispatcher.dispatch(message);
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }
    }

}
