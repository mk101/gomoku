package kolesov.maxim.server.service.game;

import kolesov.maxim.common.dto.Color;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerService {

    private final Map<UUID, Color> players = new ConcurrentHashMap<>();
    private final Set<UUID> readySet = ConcurrentHashMap.newKeySet();

    public void addPlayer(UUID id, Color color) throws IllegalStateException {
        if (getPlayer(color).isPresent()) {
            throw new IllegalStateException("Player with this color is already exists");
        }

        players.put(id, color);
    }

    public void swapColors() throws IllegalStateException {
        players.replaceAll((id, color) -> getOpposite(color));
    }

    public void deletePlayer(UUID id) {
        readySet.remove(id);
        players.remove(id);
    }

    public Optional<UUID> getPlayer(Color color) {
        if (color == Color.EMPTY) {
            throw new IllegalArgumentException("Color is empty");
        }

        return players.entrySet().stream()
                .filter(e -> e.getValue().equals(color))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public Set<UUID> getPlayers() {
        return players.keySet();
    }

    public Optional<Color> getColor(UUID id) {
        return Optional.ofNullable(players.get(id));
    }

    public void ready(UUID id) {
        readySet.add(id);
    }

    public boolean isPlayersReady() {
        return readySet.size() == 2;
    }

    public void clearReady() {
        readySet.clear();
    }

    private Color getOpposite(Color color) {
        return switch (color) {
            case WHITE -> Color.BLACK;
            case BLACK -> Color.WHITE;
            default -> throw new IllegalArgumentException("Expected black or white color");
        };
    }

}
