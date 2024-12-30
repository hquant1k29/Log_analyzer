package controller;

import javafx.scene.chart.*;
import javafx.scene.control.*;
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
    private XYChart.Series<String, Number> series = new XYChart.Series<>();
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

        // test
        xAxis.setLabel("Time");
        yAxis.setLabel("Number of request");

        // Tạo BarChart
        //barChart.setTitle("Number of request in hour");

        // Tạo dữ liệu

        series.setName("Request by time");

        // Thêm dữ liệu cho từng giờ
        int[] requests = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (int hour = 0; hour < 24; hour++) {
            series.getData().add(new XYChart.Data<>(hour + "h", requests[hour]));
        }

        // Thêm series vào biểu đồ
         barChart.getData().add(series);
        // loadData x = new loadData();
        //ArrayList<AccessLog> list = x.load();
       // PieChart.Data FailData = new PieChart.Data("404", 0);  // Số lượng POST requests
       // PieChart.Data SucData = new PieChart.Data("200", 100);    // Số lượng GET requests
//        PieChart.Data deleteData = new PieChart.Data("DELETE", 0); // Số lượng DELETE requests
//        PieChart.Data putData = new PieChart.Data("PUT", 0);    // Số lượng PUT requests
       //  Thêm dữ liệu vào PieChart
       // pieChart.getData().addAll(FailData, SucData);
       //  Thêm dữ liệu mẫu để kiểm tra
        //toRequest.setText(Integer.toString(x.getTotal()));
        //String toSizeString = String.format("%.2f",x.getTotalSize());
        //toSize.setText(toSizeString + "  Gb");
        //toFail.setText(Integer.toString(x.getTotalFail()));
       // masterData.addAll(list);
        //tableView.setItems(masterData);
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
        //System.out.println("Number of request in date" + s.getDateMap().get("2024-12-13"));
        try {
            String text = searchField.getText();
            nlist = s.Search(text,nbegin,nend,beginT,endT);
            toRequest.setText(Integer.toString(s.getTotal()));
            LocalDate begin = beginDate.getValue();
            LocalDate end = endDate.getValue();

        } catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
        series.setName("Request by time");
        HashMap<String,Integer> time = s.getTimeMap();
        drawData(nend,time);
        // Thêm dữ liệu cho từng giờ
        //int[] requests = {s.getTimeMap().get(nbegin + "00"), 0, s.getTimeMap().get(nbegin + "02"), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        for (int hour = 0; hour < 24; hour++) {
//            series.getData().add(new XYChart.Data<>(hour + "h", requests[hour]));
//        }

        // Thêm series vào biểu đồ
        //barChart.getData().add(series);
        //PieChart.Data FailData = new PieChart.Data("404", s.getStatusMap().get(404));  // Số lượng POST requests
        //PieChart.Data SucData = new PieChart.Data("200", s.getStatusMap().get(200));    // Số lượng GET requests
        //pieChart.getData().addAll(FailData, SucData);
        //System.out.println("Number of request in date" + s.getTimeMap().get(nbegin + "02"));
//        for(int i = 0; i <= 100; i++){
//            filteredData.add(nlist.get(i));
//        }
         filteredData.addAll(nlist);
        tableView.setItems(filteredData);
        //filteredData.clear();
    }
    public void drawData(String date, HashMap<String,Integer> timeMap){
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
            series.getData().add(new XYChart.Data<>(hour + "h", requests[hour]));
        }
        barChart.getData().clear();
        barChart.getData().add(series);
    }
}
