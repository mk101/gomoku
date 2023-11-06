package kolesov.maxim.server.runnable;

import kolesov.maxim.server.config.ServerConfig;
import kolesov.maxim.server.model.socket.ServerState;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.socket.SendService;
import kolesov.maxim.server.service.socket.UserRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class ServerListener implements Runnable {

    private final ServerConfig serverConfig;
    private final ServerState serverState;
    private final BlockingQueue<Message> messageQueue;
    private final UserRegisterService userRegisterService;
    private final SendService sendService;

    @Override
    @SneakyThrows
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(serverConfig.getConnectionCount());
        try (ServerSocket socket = new ServerSocket(
                serverConfig.getPort(), serverConfig.getConnectionCount(), InetAddress.getByName(serverConfig.getHost()))) {

            while (!serverState.isShutdown()) {
                Socket client = socket.accept();
                pool.execute(new ClientHandler(client, serverState, messageQueue, userRegisterService, sendService));
            }
        }

        pool.close();
    }

}
