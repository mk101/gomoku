package kolesov.maxim.client.event;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import kolesov.maxim.client.runnable.Connection;
import kolesov.maxim.common.dto.MessageAction;
import kolesov.maxim.common.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ButtonClickEventHandler implements EventHandler<ActionEvent> {

    private final Connection connection;

    @Override
    public void handle(ActionEvent e) {
        log.debug("Action {}", e);
        if (!(e.getSource() instanceof Button button)) {
            return;
        }

        String id = button.getId();
        int width = Integer.parseInt(id.split("_")[0]);
        int height = Integer.parseInt(id.split("_")[1]);

        connection.sendMessage(new MessageDto(MessageAction.MOVE, Map.of("x", width, "y", height)));
    }

}
