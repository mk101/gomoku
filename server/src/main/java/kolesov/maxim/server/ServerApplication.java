package kolesov.maxim.server;

import kolesov.maxim.common.context.ApplicationContext;
import kolesov.maxim.server.context.ServerApplicationContext;
import kolesov.maxim.server.service.LaunchService;

public class ServerApplication {

    public static void main(String[] args) {
        ApplicationContext.initialize(ServerApplicationContext.class);

        ApplicationContext.get().<LaunchService>getComponent("launchService").launch();
    }

}
