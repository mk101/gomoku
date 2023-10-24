package kolesov.maxim.server.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerStateConfig {

    private transient boolean isShutdown = false;

}
