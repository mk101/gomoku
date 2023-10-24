package kolesov.maxim.server.context;

import kolesov.maxim.common.context.ApplicationContext;
import kolesov.maxim.server.config.ServerConfig;
import kolesov.maxim.server.config.ServerStateConfig;
import kolesov.maxim.server.dispatcher.MessageDispatcher;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.LaunchService;
import kolesov.maxim.server.service.socket.SendService;
import kolesov.maxim.server.service.socket.UserRegisterService;

import java.util.concurrent.ArrayBlockingQueue;

public class ServerApplicationContext extends ApplicationContext {

    private static final int MESSAGE_QUEUE_CAPACITY = 1024;

    @Override
    protected void initializeContext() {
        registerComponent("serverConfig", new ServerConfig());
        registerComponent("serverStateConfig", new ServerStateConfig());

        registerComponent("messageQueue", new ArrayBlockingQueue<Message>(MESSAGE_QUEUE_CAPACITY));

        registerComponent("userRegisterService", new UserRegisterService());
        registerComponent("sendService", new SendService(get("userRegisterService")));

        registerComponent("messageDispatcher", new MessageDispatcher());

        registerComponent("launchService", new LaunchService(
            get("serverConfig"), get("serverStateConfig"), get("messageQueue"),
            get("userRegisterService"), get("sendService"), get("messageDispatcher")));
    }

    private <T> T get(String name) {
        return getComponent(name);
    }

}
