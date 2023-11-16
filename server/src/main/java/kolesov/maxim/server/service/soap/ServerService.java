package kolesov.maxim.server.service.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import kolesov.maxim.server.model.State;
import kolesov.maxim.server.service.game.GameService;
import kolesov.maxim.server.service.game.PlayerService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@WebService(name = "Server", serviceName = "ServerService")
@RequiredArgsConstructor
public class ServerService {

    private final GameService gameService;
    private final PlayerService playerService;

    @WebMethod
    public UUID connect() {
        UUID id = UUID.randomUUID();
        gameService.registerPlayer(id);
        return id;
    }

    @WebMethod
    public void disconnect(UUID playerId) {
        playerService.deletePlayer(playerId);
    }

    @WebMethod
    public void move(UUID playerId, int x, int y) {
        if (!playerId.equals(gameService.getCurrentPlayer())) {
            return;
        }
        gameService.move(x, y);
    }

    @WebMethod
    public void ready(UUID player) {
        playerService.ready(player);

        if (playerService.isPlayersReady()) {
            gameService.startGame();
        }
    }

    @WebMethod
    public State getState() {
        return gameService.getState();
    }

    @WebMethod
    public UUID checkWin() {
        return gameService.getWinner();
    }

}
