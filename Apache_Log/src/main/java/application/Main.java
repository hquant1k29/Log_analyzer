package application;
import log.AccessLog;
import utils.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
        ArrayList<AccessLog> result = new ArrayList<>();
        //stringCompare s = new stringCompare();
        //int n = s.BoyerMoore("Request 127.0.0.1 GET HTTP","GET");
        //System.out.println(n);
        System.out.println("Working Directory: " + System.getProperty("apache_logs.txt"));
        searchEngine s = new searchEngine();
        result = s.Search("85.170.84.143");
        for(AccessLog log : result){
            System.out.println(log.getIp());
            System.out.println(log.getLog());
            System.out.println(log.getUserID());
            System.out.println(log.getDateTime());
            System.out.println(log.getRequest());
            System.out.println(log.getStatus());
            System.out.println(log.getSize());
            System.out.println(log.getReferer());
            System.out.println(log.getUserAgent());
        }

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
