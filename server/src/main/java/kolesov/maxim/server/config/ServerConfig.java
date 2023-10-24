package kolesov.maxim.server.config;

import kolesov.maxim.common.context.ApplicationContext;
import lombok.Getter;

@Getter
public class ServerConfig {

    private final String host;
    private final int port;
    private final int connectionCount;

    public ServerConfig() {
        ApplicationContext context = ApplicationContext.get();

        host = context.getProperty("host");
        port = Integer.parseInt(context.getProperty("port"));
        connectionCount = Integer.parseInt(context.getProperty("connection-count"));
    }

}
