package kolesov.maxim.server.service.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kolesov.maxim.common.config.GameConfig;
import kolesov.maxim.common.dto.MessageDto;
import kolesov.maxim.common.dto.Color;
import kolesov.maxim.common.dto.PlayerDto;
import kolesov.maxim.common.dto.StateDto;
import kolesov.maxim.server.service.socket.SendService;
import kolesov.maxim.server.utils.WinChecker;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static kolesov.maxim.common.dto.MessageAction.*;

@Slf4j
public class GameService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final PlayerService playerService;
    private final GameConfig config;
    private final SendService sendService;

    private Color[][] map;

    @Getter
    private UUID currentPlayer;

    @Getter
    private transient boolean gameStarted;

    public GameService(PlayerService playerService, GameConfig config, SendService sendService) {
        this.playerService = playerService;
        this.config = config;
        this.sendService = sendService;


        this.map = new Color[config.getFieldWidth()][config.getFieldHeight()];
        for (int i = 0; i < config.getFieldWidth(); i++) {
            for (int j = 0; j < config.getFieldHeight(); j++) {
                this.map[i][j] = Color.EMPTY;
            }
        }
        this.currentPlayer = null;
        this.gameStarted = false;
    }

    public void registerPlayer(UUID id) throws IllegalStateException {
        if (isGameStarted()) {
            throw new IllegalStateException("Game is started");
        }

        if (playerService.getPlayer(Color.BLACK).isEmpty()) {
            playerService.addPlayer(id, Color.BLACK);
            return;
        }

        if (playerService.getPlayer(Color.WHITE).isEmpty()) {
            playerService.addPlayer(id, Color.WHITE);
            return;
        }

        throw new IllegalStateException("There are already 2 players");
    }

    public void startGame() throws IllegalStateException {
        if (playerService.getPlayers().size() != 2) {
            throw new IllegalStateException("Not enough players");
        }

        if (gameStarted) {
            throw new IllegalStateException("Game is already started");
        }

        currentPlayer = playerService.getPlayer(Color.BLACK).orElseThrow(() -> new NullPointerException("Can't find black player"));
        gameStarted = true;
        map = new Color[config.getFieldWidth()][config.getFieldHeight()];
        for (int i = 0; i < config.getFieldWidth(); i++) {
            for (int j = 0; j < config.getFieldHeight(); j++) {
                map[i][j] = Color.EMPTY;
            }
        }

        sendState();
    }

    public void stopGame() throws IllegalStateException {
        if (!gameStarted) {
            throw new IllegalStateException("Game is already stopped");
        }

        currentPlayer = null;
        gameStarted = false;

        playerService.clearReady();

        sendState();
    }

    @SneakyThrows
    public void move(int x, int y) {
        Color color = playerService.getColor(currentPlayer)
                .orElseThrow(() -> new NullPointerException(String.format("Player %s not found", currentPlayer)));

        if (map[x][y] != Color.EMPTY) {
            throw new IllegalArgumentException(String.format("Filed %d %d is already peeked", x, y));
        }

        map[x][y] = color;

        if (WinChecker.check(map, x, y)) {
            sendService.sendMessage(new MessageDto(WIN, Map.of("user", currentPlayer)));
            stopGame();
            playerService.swapColors();
            return;
        }

        currentPlayer = playerService.getPlayers().stream()
                .filter(u -> !u.equals(currentPlayer))
                .findAny().orElseThrow(() -> new IllegalStateException("Expected 2 players"));

        sendState();
    }

    private void sendState() {
        StateDto state = StateDto.builder()
            .isGameRunning(gameStarted)
            .currentPlayer(currentPlayer)
            .players(playerService.getPlayers().stream()
                .map(u -> new PlayerDto(u, playerService.getColor(u).orElseThrow(() -> new NullPointerException("Player not found"))))
                .collect(Collectors.toSet()))
            .map(map)
        .build();

        MessageDto message = new MessageDto(STATE, OBJECT_MAPPER.convertValue(state, new TypeReference<Map<String, Object>>() {}));
        sendService.sendMessage(message);
    }

}
