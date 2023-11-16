package kolesov.maxim.server.service;

import jakarta.xml.ws.Endpoint;
import kolesov.maxim.server.config.ServerConfig;
import kolesov.maxim.server.service.soap.ServerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LaunchService {

    private final ServerConfig serverConfig;
    private final ServerService serverService;

    public void launch() {
        String rootPath = String.format("http://%s:%s/", serverConfig.getHost(), serverConfig.getPort());
        Endpoint.publish(rootPath, serverService);
    }

}
