package kolesov.maxim.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kolesov.maxim.client.context.ClientApplicationContext;
import kolesov.maxim.client.controller.GameController;
import kolesov.maxim.common.context.ApplicationContext;

import java.io.IOException;

public class ClientApplication extends Application {

    private GameController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Gomoku!");
        stage.setScene(scene);

        controller = fxmlLoader.getController();

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        controller.stop();
    }

    public static void main(String[] args) {
        ApplicationContext.initialize(ClientApplicationContext.class);
        launch();
    }
}