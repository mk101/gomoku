package kolesov.maxim.client.controller;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kolesov.maxim.client.event.ButtonClickEventHandler;
import kolesov.maxim.client.runnable.Connection;
import kolesov.maxim.common.config.GameConfig;
import kolesov.maxim.common.context.ApplicationContext;
import kolesov.maxim.common.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GameController {

    private static final PseudoClass EMPTY_CLASS = PseudoClass.getPseudoClass("empty");
    private static final PseudoClass BLACK_CLASS = PseudoClass.getPseudoClass("black");
    private static final PseudoClass WHITE_CLASS = PseudoClass.getPseudoClass("white");

    private static final double BUTTON_SIZE = 30.0;

    private final GameConfig config;

    private Connection connection;

    public GameController() {
        this.config = ApplicationContext.get().getComponent("gameConfig");
    }


    @FXML
    private VBox root;

    @FXML
    private Button readyButton;

    @FXML
    private Label colorLabel;

    public void stop() {
        connection.sendMessage(new MessageDto(MessageAction.DISCONNECT, Map.of()));
        connection.setClose(true);
    }

    @FXML
    private void initialize() {
        connection = new Connection(ApplicationContext.get().getComponent("clientConfig"), this::updateState, this::onWin);
        Thread connectionThread = new Thread(connection);
        connectionThread.setDaemon(true);
        connectionThread.setName("connection-thread");
        connectionThread.start();

        int width = config.getFieldWidth();
        int height = config.getFieldHeight();
        for (int i = 0; i < height; i++) {
            HBox line = new HBox();
            for (int j = 0; j < width; j++) {
                Button button = new Button();
                button.pseudoClassStateChanged(EMPTY_CLASS, true);

                button.setMinWidth(BUTTON_SIZE);
                button.setMinHeight(BUTTON_SIZE);
                button.setMaxWidth(BUTTON_SIZE);
                button.setMaxHeight(BUTTON_SIZE);

                button.setDisable(true);

                button.setId(String.format("%d_%d", j, i));

                button.setOnAction(new ButtonClickEventHandler(connection));

                line.getChildren().add(button);
            }
            root.getChildren().add(line);
        }

        log.debug("Create filed");
    }

    @FXML
    private void ready(ActionEvent actionEvent) {
        connection.sendMessage(new MessageDto(MessageAction.READY, Map.of()));
        readyButton.setDisable(true);
    }

    private void onWin(boolean isWin) {
        Platform.runLater(() -> {
            String message;
            if (isWin) {
                message = "You win";
            } else {
                message = "You loose";
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(message);
            alert.show();

            readyButton.setDisable(false);
        });
    }

    private void updateState(StateDto state) {
        if (!state.isGameRunning()) {
            disableButtons();
            readyButton.setDisable(false);
            return;
        }

        if (connection.getPlayerId().equals(state.getCurrentPlayer())) {
            activeButtons();
        } else {
            disableButtons();
        }

        Platform.runLater(() ->
                setColor(state.getPlayers().stream().filter(p -> connection.getPlayerId().equals(p.getId())).map(PlayerDto::getColor).findFirst().orElseThrow()));

        for (int i = 0; i < config.getFieldWidth(); i++) {
            for (int j = 0; j < config.getFieldHeight(); j++) {
                Button button = getButton(i,j);
                Color color = state.getMap()[i][j];

                switch (color) {
                    case EMPTY -> {
                        button.pseudoClassStateChanged(BLACK_CLASS, false);
                        button.pseudoClassStateChanged(WHITE_CLASS, false);
                        button.pseudoClassStateChanged(EMPTY_CLASS, true);
                    }
                    case BLACK -> {
                        button.pseudoClassStateChanged(BLACK_CLASS, true);
                        button.pseudoClassStateChanged(WHITE_CLASS, false);
                        button.pseudoClassStateChanged(EMPTY_CLASS, false);
                    }
                    case WHITE -> {
                        button.pseudoClassStateChanged(BLACK_CLASS, false);
                        button.pseudoClassStateChanged(WHITE_CLASS, true);
                        button.pseudoClassStateChanged(EMPTY_CLASS, false);
                    }
                }
            }
        }
    }

    private void setColor(Color color) {
        if (color == Color.BLACK) {
            colorLabel.setText("Black");
            return;
        }

        colorLabel.setText("White");
    }

    private void activeButtons() {
        for (HBox box : root.getChildren().stream().map(n -> (HBox)n).toList()) {
            for (Button btn : box.getChildren().stream().map(n -> (Button)n).toList()) {
                btn.setDisable(false);
            }
        }
    }

    private void disableButtons() {
        for (HBox box : root.getChildren().stream().map(n -> (HBox)n).toList()) {
            for (Button btn : box.getChildren().stream().map(n -> (Button)n).toList()) {
                btn.setDisable(true);
            }
        }
    }

    private Button getButton(int x, int y) {
        return (Button) ((HBox) root.getChildren().get(y)).getChildren().get(x);
    }

}