package kolesov.maxim.common.dto;

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
public class StateDto {

    private Color[][] map;

    private Set<PlayerDto> players;

    private UUID currentPlayer;

    private boolean isGameRunning;

}
