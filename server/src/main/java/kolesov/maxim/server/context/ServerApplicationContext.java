package kolesov.maxim.server.context;

import kolesov.maxim.common.config.GameConfig;
import kolesov.maxim.common.context.ApplicationContext;
import kolesov.maxim.server.config.ServerConfig;
import kolesov.maxim.server.service.LaunchService;
import kolesov.maxim.server.service.game.GameService;
import kolesov.maxim.server.service.game.PlayerService;
import kolesov.maxim.server.service.soap.ServerService;

public class ServerApplicationContext extends ApplicationContext {

    @Override
    protected void initializeContext() {
        registerComponent("serverConfig", new ServerConfig());
        registerComponent("gameConfig", new GameConfig());

        registerComponent("playerService", new PlayerService());
        registerComponent("gameService", new GameService(get("playerService"), get("gameConfig")));

        registerComponent("serverService", new ServerService(get("gameService"), get("playerService")));

        registerComponent("launchService", new LaunchService(
            get("serverConfig"), get("serverService")));
    }

    private <T> T get(String name) {
        return getComponent(name);
    }

}
