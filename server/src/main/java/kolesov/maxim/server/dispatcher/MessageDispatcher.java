package kolesov.maxim.server.dispatcher;

import kolesov.maxim.common.dto.MessageDto;
import kolesov.maxim.server.model.socket.Message;
import kolesov.maxim.server.service.game.GameService;
import kolesov.maxim.server.service.game.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class MessageDispatcher {

    private final PlayerService playerService;
    private final GameService gameService;

    public void dispatch(Message message) {
        MessageDto payload = message.getPayload();
        switch (payload.getAction()) {
            case CONNECTED -> gameService.registerPlayer(message.getHeader().getUserId());
            case DISCONNECT -> {
                playerService.deletePlayer(message.getHeader().getUserId());
                if (gameService.isGameStarted()) {
                    gameService.stopGame();
                }
            }
            case MOVE -> {
                UUID id = message.getHeader().getUserId();
                if (!gameService.getCurrentPlayer().equals(id)) {
                    throw new IllegalStateException(String.format("It's not this player's move now. id=%s", id));
                }

                int x = Integer.parseInt(getFromPayload("x", payload.getPayload()));
                int y = Integer.parseInt(getFromPayload("y", payload.getPayload()));
                gameService.move(x, y);
            }
            case READY -> {
                UUID id = message.getHeader().getUserId();
                playerService.ready(id);

                if (playerService.isPlayersReady()) {
                    gameService.startGame();
                }
            }
        }
    }

    private String getFromPayload(String key, Map<String, Object> payload) {
        if (!payload.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Argument %s is expected", key));
        }

        return payload.get(key).toString();
    }

}
