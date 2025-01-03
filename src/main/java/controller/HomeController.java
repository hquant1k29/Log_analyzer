package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import log.*;
import javafx.fxml.FXML;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController {
    @FXML
    private Button apacheButton;
    @FXML
    private Button ModsecButton;
    @FXML
    private Button IPTableButton;

    @FXML
    private AnchorPane apache;
    @FXML
    private AnchorPane modsec;
    @FXML
    private AnchorPane iptable;
    @FXML
    private TextField beginTime;
    @FXML
    private TextField endTime;
    @FXML
    private Label toRequest;
    @FXML
    private Label toFail;
    @FXML
    private Label toSize;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TableView<AccessLog> tableView;
    @FXML
    private TableColumn<AccessLog, String> ipColumn;
    @FXML
    private TableColumn<AccessLog, String> logColumn;
    @FXML
    private TableColumn<AccessLog, String> userIDColumn;
    @FXML
    private TableColumn<AccessLog, String> dateColumn;
    @FXML
    private TableColumn<AccessLog, String> timeColumn;
    @FXML
    private TableColumn<AccessLog, String> requestColumn;
    @FXML
    private TableColumn<AccessLog, Integer> statusColumn;
    @FXML
    private TableColumn<AccessLog, Integer> sizeColumn;
    @FXML
    private TableColumn<AccessLog, String> refererColumn;
    @FXML
    private TableColumn<AccessLog, String> userAgentColumn;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> DateChart;
    @FXML
    private BarChart<String, Number> TimeChart;
    @FXML
    private BarChart<String, Number> TimeDetailChart;


    public void initialize() {
        iptable.setVisible(true);
        iptable.setMouseTransparent(false);
        modsec.setVisible(false);
        modsec.setMouseTransparent(true);
        apache.setVisible(false);
        apache.setMouseTransparent(true);

        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        logColumn.setCellValueFactory(new PropertyValueFactory<>("log"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        requestColumn.setCellValueFactory(new PropertyValueFactory<>("request"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        refererColumn.setCellValueFactory(new PropertyValueFactory<>("referer"));
        userAgentColumn.setCellValueFactory(new PropertyValueFactory<>("userAgent"));

        switchChart(DateChart,TimeChart,TimeDetailChart);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        for (int hour = 0; hour < 24; hour++) {
            series1.getData().add(new XYChart.Data<>(hour + "h", 0));
        }
        TimeChart.getData().add(series1);
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        for (int hour = 0; hour < 30; hour++) {
            series2.getData().add(new XYChart.Data<>(hour + "h", 0));
        }
        TimeDetailChart.getData().add(series2);
        // switchChart(DateChart,TimeChart);
//        searchEngine s = new searchEngine();
//        s.Search("","2015-05-15","2015-05-25","","");
//        LocalDate begin = LocalDate.of(2015,5,15);
//        LocalDate end = LocalDate.of(2015,5,25);
//
//        drawChartByDate(begin,end,s.getTimeMap());

        //NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

//        drawChartByTime("2015-05-19",s.getTimeMap());

    }
    public void switchApache(){
        iptable.setVisible(false);
        iptable.setMouseTransparent(true);
        modsec.setVisible(false);
        modsec.setMouseTransparent(true);
        apache.setVisible(true);
        apache.setMouseTransparent(false);
        apacheButton.setStyle("-fx-background-color: #404a93;");
        ModsecButton.setStyle("-fx-background-color: #576aca;");
        IPTableButton.setStyle("-fx-background-color: #576aca;");
    }
    public void Search(){
        String nbegin = "",nend = "";
        String beginT = beginTime.getText();
        String endT = endTime.getText();
        LocalDate beginD = beginDate.getValue();
        LocalDate endD = endDate.getValue();

        ObservableList<AccessLog> filteredData = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(beginD != null){
            nbegin = beginD.format(formatter);
        }
        if(endD != null){
            nend = endD.format(formatter);
        }
        searchEngine s = new searchEngine();
        ArrayList<AccessLog> nlist = new ArrayList<>();

        try {
            String text = searchField.getText();
            nlist = s.Search(text,nbegin,nend,beginT,endT);
            toRequest.setText(Integer.toString(s.getTotal()));

        } catch(NullPointerException e){
            System.out.println(e.getMessage());
        }

        HashMap<String,Integer> time = s.getTimeMap();
        if(!nend.equals(nbegin)){
            switchChart(DateChart,TimeChart,TimeDetailChart);
            drawChartByDate(beginD,endD,time);
        }
        else{
            if(!endT.isEmpty() && !beginT.isEmpty()){
                switchChart(TimeDetailChart,DateChart,TimeChart);
                System.out.println("X:" + beginT.substring(0,2));
                drawChartbyDetailTime(beginT.substring(0,2),nbegin,time);
            }
            else{
                switchChart(TimeChart,DateChart,TimeDetailChart);
                drawChartByTime(nend,time);
            }
        }
        int [] statusMap = s.getStatusMap();
        drawPieChart(statusMap);
        // Thêm dữ liệu cho từng giờ
        filteredData.addAll(nlist);
        tableView.setItems(filteredData);
        //filteredData.clear();
    }
    public void setInforSearch(String nbegin,String nend,String beginT,String endT){
        LocalDate nBegin = LocalDate.parse(nbegin);
        LocalDate nEnd = LocalDate.parse(nend);
        beginDate.setValue(nBegin);
        endDate.setValue(nEnd);
        endTime.setText(endT);
        beginTime.setText(beginT);
    }
    public void drawChartbyDetailTime(String hour, String date, HashMap<String,Integer> timeMap){
        TimeDetailChart.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) TimeDetailChart.getXAxis();
        xAxis.getCategories().clear();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        if(hour.length() == 1){ hour = "0" + hour;}
        int[] requests = new int[61];
        for(int i = 0; i < 60; i++){
            try {
                if(i < 10) requests[i] = timeMap.getOrDefault(date + hour + ":0" + i,0);
                else requests[i] = timeMap.getOrDefault(date + hour + ":" + i,0);

            } catch(NullPointerException e){
                System.out.println(e.getMessage());
            }
        }

        for(int i = 0; i < 60; i+= 2){
            int value = requests[i] + requests[i+1];
            String time;
            if(i < 10){
                time = hour + ":0" + i;
            } else{
                time = hour + ":" + i;
            }

            XYChart.Data<String, Number> data = new XYChart.Data<>(date +" "+ time, value);
            System.out.println(time + ": " + value);
            series1.getData().add(data);
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip("Time: "+ date +" " + time + "\nValue: " + value);
                    Tooltip.install(newNode, tooltip);
                }
            });
        }
        TimeDetailChart.getData().add(series1);
    }

    public void drawChartByTime(String date, HashMap<String,Integer> timeMap){
        switchChart(TimeChart,DateChart,TimeDetailChart);
        TimeChart.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) TimeChart.getXAxis();
        xAxis.getCategories().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Number of Request on " + date);
        int[] requests = new int[24];
        for(int i = 0; i < 24; i++){
            try {
                if(i < 10) requests[i] = timeMap.getOrDefault(date + "0" + i,0);
                else requests[i] = timeMap.getOrDefault(date + i,0);

            } catch(NullPointerException e){
                System.out.println(e.getMessage());
            }
        }
        for (int hour = 0; hour < 24; hour++) {
            String curHour = hour + "h";
            int value = requests[hour];
            XYChart.Data<String, Number> data = new XYChart.Data<>(curHour, value);
            series1.getData().add(data);
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip("Time: " + curHour + "\nValue: " + value);
                    Tooltip.install(newNode, tooltip);
                }
            });
        }
        TimeChart.getData().add(series1);
        for (XYChart.Data<String, Number> item : series1.getData()) {
            item.getNode().setOnMousePressed((MouseEvent event) -> {
                String hourPicked = item.getXValue();
                hourPicked = hourPicked.substring(0, hourPicked.length() - 1);
                int tmp = Integer.parseInt(hourPicked) + 1;
                String nextT = String.valueOf(tmp);

                if(hourPicked.length() == 1){
                    hourPicked = "0"+ hourPicked + ":00";
                } else{
                    hourPicked =  hourPicked + ":00";
                }
                if(nextT.length() == 1){
                    nextT = "0"+ nextT + ":00";
                } else{
                    nextT =  nextT + ":00";
                }
                setInforSearch(date,date,hourPicked,nextT);
                this.Search();
                System.out.println("Filter by this colume: " + item.getXValue());
            });
        }
    }
    public void drawChartByDate(LocalDate nbgin,LocalDate nend, HashMap<String,Integer> timeMap){

        DateChart.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) DateChart.getXAxis();
        xAxis.getCategories().clear();
        xAxis.setAutoRanging(false);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Number of Request From " + nbgin + " To " + nend);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = nbgin;
        while (!currentDate.isAfter(nend)) {
            String curDate = currentDate.format(formatter);
            xAxis.getCategories().add(curDate);
            int value = timeMap.getOrDefault(curDate, 0);
            XYChart.Data<String, Number> data = new XYChart.Data<>(curDate, value);
            series1.getData().add(data);

            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip("Date: " + curDate + "\nValue: " + value);
                    Tooltip.install(newNode, tooltip);
                }
            });

            System.out.println(curDate +": " + timeMap.getOrDefault(curDate,0));
            System.out.println(currentDate); // In ngày theo định dạng dd-MM-yyyy
            currentDate = currentDate.plusDays(1); // Tăng ngày thêm 1 ngày
        }

        DateChart.getData().add(series1);
        for (XYChart.Data<String, Number> item : series1.getData()) {
            item.getNode().setOnMousePressed((MouseEvent event) -> {
                String datePicked = item.getXValue();
                setInforSearch(datePicked,datePicked,"","");
                this.Search();
                System.out.println("Filter by this colume: " + item.getXValue());
            });
        }
    }
    public void drawPieChart(int [] StatusMap){
        pieChart.getData().clear();
        for(int i = 100; i <= 520; i++){
            if(StatusMap[i] != 0){
                PieChart.Data slice = new PieChart.Data("" + i,StatusMap[i]);
                pieChart.getData().add(slice); // Thêm phần tử vào PieChart
            }
        }
    }

    public void switchChart(BarChart<String,Number> X,BarChart<String,Number> Y,BarChart<String,Number> Z){
        X.setVisible(true);
        X.setMouseTransparent(false);
        Y.setVisible(false);
        Y.setMouseTransparent(true);
        Z.setVisible(false);
        Z.setMouseTransparent(true);
    }

    // IPTable Controller


    // ModsecController

}
