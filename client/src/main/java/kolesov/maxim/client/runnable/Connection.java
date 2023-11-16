package kolesov.maxim.client.runnable;

import generated.Server;
import generated.ServerService;
import generated.State;
import kolesov.maxim.client.config.ClientConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@RequiredArgsConstructor
public class Connection implements Runnable {

    private static final String URL = "http://%s:%s/?WSDL";

    private final ClientConfig clientConfig;

    private final Consumer<State> updateState;
    private final Consumer<Boolean> onWin;

    @Getter
    private UUID playerId;

    @Setter
    private volatile boolean isClose = false;
    private Server server;

    private boolean winnerCheck;

    @Override
    @SneakyThrows
    public void run() {
        server = new ServerService(new URI(String.format(URL, clientConfig.getHost(), clientConfig.getPort())).toURL()).getServerPort();
        playerId = UUID.fromString(server.connect());

        while (!isClose) {
            String winner = server.checkWin();
            if (!winnerCheck && winner != null) {
                winnerCheck = true;
                onWin.accept(UUID.fromString(winner).equals(playerId));
            }

            if (winner == null) {
                winnerCheck = false;
            }

            State state = server.getState();
            updateState.accept(state);

            MILLISECONDS.sleep(100);
        }

        server.disconnect(playerId.toString());
    }


    public void move(int x, int y) {
        server.move(playerId.toString(), x, y);
    }

    public void ready() {
        server.ready(playerId.toString());
    }

}
