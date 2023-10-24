package kolesov.maxim.server.runnable;

import kolesov.maxim.server.config.ServerStateConfig;
import kolesov.maxim.server.dispatcher.MessageDispatcher;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.socket.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

@Slf4j
@RequiredArgsConstructor
public class MessageListener implements Runnable {

    private final Queue<Message> messageQueue;
    private final ServerStateConfig serverStateConfig;
    private final SendService sendService;
    private final MessageDispatcher messageDispatcher;

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void run() {
        while (!serverStateConfig.isShutdown()) {
            Message message = messageQueue.poll();
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
