package kolesov.maxim.server;

import kolesov.maxim.common.context.ApplicationContext;
import kolesov.maxim.server.context.ServerApplicationContext;

public class ServerApplication {

    public static void main(String[] args) {
        ApplicationContext.initialize(ServerApplicationContext.class);
    }

}
