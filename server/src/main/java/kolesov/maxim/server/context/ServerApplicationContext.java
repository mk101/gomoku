package kolesov.maxim.server.context;

import kolesov.maxim.common.config.GameConfig;
import kolesov.maxim.common.context.ApplicationContext;
import kolesov.maxim.server.config.ServerConfig;
import kolesov.maxim.server.model.socket.ServerState;
import kolesov.maxim.server.dispatcher.MessageDispatcher;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.LaunchService;
import kolesov.maxim.server.service.game.GameService;
import kolesov.maxim.server.service.game.PlayerService;
import kolesov.maxim.server.service.socket.SendService;
import kolesov.maxim.server.service.socket.UserRegisterService;

import java.util.concurrent.ArrayBlockingQueue;

public class ServerApplicationContext extends ApplicationContext {

    private static final int MESSAGE_QUEUE_CAPACITY = 1024;

    @Override
    protected void initializeContext() {
        registerComponent("serverConfig", new ServerConfig());
        registerComponent("gameConfig", new GameConfig());
        registerComponent("serverState", new ServerState());

        registerComponent("messageQueue", new ArrayBlockingQueue<Message>(MESSAGE_QUEUE_CAPACITY));

        registerComponent("userRegisterService", new UserRegisterService());
        registerComponent("sendService", new SendService(get("userRegisterService")));

        registerComponent("playerService", new PlayerService());
        registerComponent("gameService", new GameService(get("playerService"), get("gameConfig"),
                get("sendService")));

        registerComponent("messageDispatcher", new MessageDispatcher(get("playerService"), get("gameService")));

        registerComponent("launchService", new LaunchService(
            get("serverConfig"), get("serverState"), get("messageQueue"),
            get("userRegisterService"), get("sendService"), get("messageDispatcher")));
    }

    private <T> T get(String name) {
        return getComponent(name);
    }

}
