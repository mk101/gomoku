package kolesov.maxim.client.config;

import kolesov.maxim.common.context.ApplicationContext;
import lombok.Getter;

@Getter
public class ClientConfig {

    private final String host;
    private final int port;

    public ClientConfig() {
        ApplicationContext context = ApplicationContext.get();

        host = context.getProperty("host");
        port = Integer.parseInt(context.getProperty("port"));
    }

}
