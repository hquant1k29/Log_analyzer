package controller;

import javafx.scene.Parent;
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
import java.util.List;


public class ApacheController {
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
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    private final ObservableList<AccessLog> masterData = FXCollections.observableArrayList();
    @FXML
    public void initialize() {
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

//        searchEngine s = new searchEngine();
//        s.Search("","2015-05-15","2015-05-25","","");
//        LocalDate begin = LocalDate.of(2015,5,15);
//        LocalDate end = LocalDate.of(2015,5,25);
//
//        drawChartByDate(begin,end,s.getTimeMap());

        //NumberAxis yAxis = (NumberAxis) barChart.getYAxis();

//        drawChartByTime("2015-05-19",s.getTimeMap());

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
            drawChartByDate(beginD,endD,time);
        }
        else{
            drawChartByTime(nend,time);

        }
        int [] statusMap = s.getStatusMap();
        drawPieChart(statusMap);
        // Thêm dữ liệu cho từng giờ

        filteredData.addAll(nlist);
        tableView.setItems(filteredData);
        //filteredData.clear();
    }
    public void drawChartByTime(String date, HashMap<String,Integer> timeMap){
        // XYChart.Series<String, Number> tmp = barChart.getData().getLast();
        // CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
        // xAxis.getCategories().clear();
        // xAxis.setAutoRanging(false);
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                series.getData().remove(data);
            }
        }


        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

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
            series1.getData().add(new XYChart.Data<>(hour + "h", requests[hour]));
        }
        barChart.getData().add(series1);
        barChart.layout();
    }
    public void drawChartByDate(LocalDate nbgin,LocalDate nend, HashMap<String,Integer> timeMap){
        int count = 0;

//        barChart.getData().clear();

        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                series.getData().remove(data);
            }
        }
//        CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
//        xAxis.setAutoRanging(false);
//        xAxis.getCategories().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = nbgin;
        while (!currentDate.isAfter(nend)) {
            count ++;
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

        if(count >= 12) xAxis.setTickLabelRotation(90);
        barChart.getData().add(series1);
        for (XYChart.Data<String, Number> item : series1.getData()) {
            item.getNode().setOnMousePressed((MouseEvent event) -> {
                String datePicked = item.getXValue();
                barChart.getData().clear();
                drawChartByTime(datePicked,timeMap);
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
}
