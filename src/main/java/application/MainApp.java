package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Iptables Log Analyzer");
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("views/MainView.fxml"))));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
