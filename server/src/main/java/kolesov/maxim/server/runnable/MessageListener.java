package kolesov.maxim.server.runnable;

import kolesov.maxim.server.config.ServerStateConfig;
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
    private final ServerStateConfig serverStateConfig;
    private final SendService sendService;
    private final MessageDispatcher messageDispatcher;

    @Override
    @SneakyThrows
    public void run() {
        while (!serverStateConfig.isShutdown()) {
            Message message = messageQueue.take();
            if (message.getHeader().getDirection() == Message.MessageDirection.OUT) {
                if (message.getHeader().getUserId() == null) {
                    sendService.sendMessage(message.getPayload());
                    continue;
                }

                sendService.sendMessage(message.getHeader().getUserId(), message.getPayload());
                continue;
            }

            messageDispatcher.dispatch(message);
        }
    }

}
