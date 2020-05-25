package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class FamilyManagement extends Application {
    public static Stage stage = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        primaryStage.setOnShowing(ev -> {
            try {
                ServerConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        primaryStage.setOnHiding(event -> {
            try {
                ServerConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        primaryStage.initStyle(StageStyle.UNDECORATED);

        Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("main.fxml")));

        primaryStage.setScene(mainScene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
