package application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class Main extends Application{
    //private final String dirFile = "Apache_Log/src/main/resources/views/Home.fxml";
    public static void main(String[] args) {
        launch(args);

        //ArrayList<AccessLog> result = new ArrayList<>();
        //stringCompare s = new stringCompare();
        //int n = s.BoyerMoore("Request 127.0.0.1 GET HTTP","GET");
        //System.out.println(n);
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        //searchEngine s = new searchEngine();

        //loadData x = new loadData();
        //result = x.load();

//        System.out.println("Total Request:" + x.getTotal());
//        System.out.println("Total size Request:" + x.getTotalSize());
//        System.out.println("Total Fail:" + x.getTotalFail());
//        System.out.println("Total Fail:" + x.getProto().get("GET"));
//        System.out.println("Total Request in 2015-05-18:" + x.getDateMap().get("2015-05-18"));
//        System.out.println("Total Request in 2015-05-18:" + x.getTimeMap().get("2015-05-1804"));
       // result = s.Search("");
        //result = s.SearchByDate(result,"2015-05-16","2015-05-18");
        //int dem = 0;
       // for(AccessLog log : result){
            //System.out.println(log.getIp());
            //System.out.println(log.getLog());
            //System.out.println(log.getUserID());
            //System.out.println(log.getDate() + log.getTime());
            //System.out.println(log.getRequest());
            //System.out.println(log.getStatus());
            //System.out.println(log.getSize());
            //System.out.println(log.getReferer());
            //System.out.println(log.getUserAgent());
            //dem ++;
        //}
        //System.out.println(dem);

    }

    @Override
    public void start(Stage stage) {
        try{
            stage.setTitle("Apache Log");
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            // Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Apache_log.fxml")));
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Home.fxml")));
            Scene scene = new Scene(root,1280,800);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
