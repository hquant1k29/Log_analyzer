package application;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import utils.ipCountry;
import utils.ipCountry;
import utils.searchEngine;

import java.io.File;
import java.net.InetAddress;
import java.util.Objects;

public class Main extends Application{
    //private final String dirFile = "Apache_Log/src/main/resources/views/Home.fxml";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try{
            stage.setTitle("Monitor Log");
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            // Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Apache_log.fxml")));
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Home.fxml")));
            Scene scene = new Scene(root,1400,700);
            stage.setScene(scene);
            stage.setWidth(1400);
            stage.setHeight(700);
            //stage.setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
            stage.show();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
