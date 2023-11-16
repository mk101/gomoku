package kolesov.maxim.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class State {

    private Color[][] map;

    private Set<Player> players;

    private UUID currentPlayer;

    private boolean isGameRunning;

}
