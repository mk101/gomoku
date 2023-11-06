package kolesov.maxim.server.model.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerState {

    private transient boolean isShutdown = false;

}
