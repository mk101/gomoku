package kolesov.maxim.server.service;

import kolesov.maxim.server.config.ServerConfig;
import kolesov.maxim.server.model.socket.ServerState;
import kolesov.maxim.server.dispatcher.MessageDispatcher;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.runnable.MessageListener;
import kolesov.maxim.server.runnable.ServerListener;
import kolesov.maxim.server.service.socket.SendService;
import kolesov.maxim.server.service.socket.UserRegisterService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class LaunchService {

    private final ServerConfig serverConfig;
    private final ServerState serverState;
    private final BlockingQueue<Message> messageQueue;
    private final UserRegisterService userRegisterService;
    private final SendService sendService;
    private final MessageDispatcher messageDispatcher;

    public void launch() {
        Thread messageListener = new Thread(new MessageListener(messageQueue, serverState, sendService, messageDispatcher));
        messageListener.setDaemon(true);
        messageListener.setName("message-listener");
        messageListener.start();

        Thread serverListener = new Thread(new ServerListener(serverConfig, serverState, messageQueue, userRegisterService, sendService));
        serverListener.setName("server-listener");
        serverListener.start();
    }

}
