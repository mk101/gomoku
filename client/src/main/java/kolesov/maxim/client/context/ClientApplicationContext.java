package kolesov.maxim.client.context;

import kolesov.maxim.client.config.ClientConfig;
import kolesov.maxim.common.config.GameConfig;
import kolesov.maxim.common.context.ApplicationContext;

public class ClientApplicationContext extends ApplicationContext {

    @Override
    protected void initializeContext() {
        registerComponent("clientConfig", new ClientConfig());
        registerComponent("gameConfig", new GameConfig());
    }

}
