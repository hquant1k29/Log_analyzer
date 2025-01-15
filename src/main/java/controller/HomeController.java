package controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import log.*;
import javafx.fxml.FXML;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.net.URL;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.IptablesLogProcessor;
import utils.LogAnalyzer;
import log.IptablesModel;
import utils.FileUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeController {
    @FXML
    private Button apacheButton;
    @FXML
    private Button ModsecButton;
    @FXML
    private Button IPTableButton;
    @FXML
    private HBox DateBox;
    @FXML
    private HBox CountryBox;
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
    private PieChart pieCountry;
    @FXML
    private BarChart<String,Number> BarCountry;
    @FXML
    private BarChart<String, Number> DateChart;
    @FXML
    private BarChart<String, Number> TimeChart;
    @FXML
    private BarChart<String, Number> TimeDetailChart;
    @FXML
    private TextField searchTextField3;  // TextField for the search keyword
    @FXML
    private DatePicker fromDate3;  // DatePicker for the start date
    @FXML
    private DatePicker toDate3;  // DatePicker for the end date
    @FXML
    private TableView<LogEntry> logTable;  // TreeTableView for displaying search results
    @FXML
    private TableColumn<LogEntry, String> idColumn3, dateColumn3, timeColumn3, ipColumn3, statusColumn3, requestUriColumn3, userAgentColumn3, messageColumn3, actionColumn3;
    @FXML
    private BarChart<String, Number> barChartMod;
    @FXML
    private PieChart pieChartMod;
    // attribute of Iptable
    @FXML
    private Label totalRequestLabelIPT;
    @FXML
    private Label totalFailLabelIPT;
    @FXML
    private Label totalSizeLabelIPT;
    @FXML
    private PieChart requestPieChartIPT;
    @FXML
    private BarChart<String, Number> logBarChartIPT;

    // Table columns
    @FXML
    private TableView<IptablesModel> logTableIP;
    @FXML
    private TableColumn<IptablesModel, String> prefixColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> sourceIPColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> destinationIPColumnIPT;
    @FXML
    private TableColumn<IptablesModel, Number> sourcePortColumnIPT;
    @FXML
    private TableColumn<IptablesModel, Number> destinationPortColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> protocolColumnIPT;
    @FXML
    private TableColumn<IptablesModel, Number> lengthColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> inInterfaceColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> outInterfaceColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> macColumnIPT;
    @FXML
    private TableColumn<IptablesModel, String> timestampColumnIPT; // hiển thị LocalDateTime as String

    // Filter controls
    @FXML
    private DatePicker fromDatePickerIPT;
    @FXML
    private DatePicker toDatePickerIPT;
    @FXML
    private TextField searchFieldIPT;

    private ObservableList<IptablesModel> masterData = FXCollections.observableArrayList();

    private final searchEngine searchEngine = new searchEngine();

    public void initialize() {
        iptable.setVisible(true);
        iptable.setMouseTransparent(false);
        modsec.setVisible(false);
        modsec.setMouseTransparent(true);
        apache.setVisible(false);
        apache.setMouseTransparent(true);
        //initialize1();
        initialize2();
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
        CountryBox.setVisible(false);
        CountryBox.setMouseTransparent(true);
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

    public void switchIPTable(){
        iptable.setVisible(true);
        iptable.setMouseTransparent(false);
        modsec.setVisible(false);
        modsec.setMouseTransparent(true);
        apache.setVisible(false);
        apache.setMouseTransparent(true);
        IPTableButton.setStyle("-fx-background-color: #404a93;");
        ModsecButton.setStyle("-fx-background-color: #576aca;");
        apacheButton.setStyle("-fx-background-color: #576aca;");
    }

    public void switchModsecTable(){
        iptable.setVisible(false);
        iptable.setMouseTransparent(true);
        modsec.setVisible(true);
        modsec.setMouseTransparent(false);
        apache.setVisible(false);
        apache.setMouseTransparent(true);
        ModsecButton.setStyle("-fx-background-color: #404a93;");
        IPTableButton.setStyle("-fx-background-color: #576aca;");
        apacheButton.setStyle("-fx-background-color: #576aca;");
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
        HashMap<String,Integer> ipMap = s.getIpMap();
        drawPieChart(statusMap);
        drawPieCountry(ipMap);
        // Thêm dữ liệu cho từng giờ
        filteredData.clear();
        for(int i = 0; i <= 200; i++){
            filteredData.add(nlist.get(i));
        }
        //filteredData.addAll(nlist);
        tableView.setItems(filteredData);
        //filteredData.clear();
    }
    public void MoreChart(){
        DateBox.setVisible(false);
        DateBox.setMouseTransparent(true);
        CountryBox.setVisible(true);
        CountryBox.setMouseTransparent(false);
    }
    public void TurnBack(){
        CountryBox.setVisible(false);
        CountryBox.setMouseTransparent(true);
        DateBox.setVisible(true);
        DateBox.setMouseTransparent(false);
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
            currentDate = currentDate.plusDays(1); // Tăng ngày thêm 1 ngày
        }

        DateChart.getData().add(series1);
        for (XYChart.Data<String, Number> item : series1.getData()) {
            item.getNode().setOnMousePressed((MouseEvent event) -> {
                String datePicked = item.getXValue();
                setInforSearch(datePicked,datePicked,"","");
                this.Search();
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
    public void drawPieCountry(HashMap<String,Integer> ipMap){
        BarCountry.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) BarCountry.getXAxis();
        xAxis.getCategories().clear();
        xAxis.setAutoRanging(false);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        pieCountry.getData().clear();
        HashMap<String,Integer> nameCountry = new HashMap<>();
        ipCountry s = new ipCountry();
        int tSum = 0;
        for (Map.Entry<String, Integer> entry : ipMap.entrySet()) {
            String tmp = s.getNameCountry(entry.getKey());
            tSum += entry.getValue();
            nameCountry.merge(tmp,entry.getValue(),Integer::sum);
        }
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(nameCountry.entrySet());
        // Sắp xếp List theo giá trị (descending)
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Lấy 5 phần tử có giá trị lớn nhất
        int count = 0,sum = 0;
        for (Map.Entry<String, Integer> entry : entryList) {
            if (count < 5) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(),entry.getValue());
                pieCountry.getData().add(slice); // Thêm phần tử vào PieChart
                // BarChart by Country
                xAxis.getCategories().add(entry.getKey());
                int value = entry.getValue();
                XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), value);
                series1.getData().add(data);

                data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip tooltip = new Tooltip("Country: " + entry.getKey() + "\nValue: " + value);
                        Tooltip.install(newNode, tooltip);
                    }
                });
                sum += entry.getValue();
                count++;
            } else {
                break;
            }
        }
//        xAxis.getCategories().add("Others");
//        XYChart.Data<String, Number> data = new XYChart.Data<>("Others",tSum - sum);
//        series1.getData().add(data);
        PieChart.Data slice = new PieChart.Data("Others",tSum - sum);
        pieCountry.getData().add(slice);
        BarCountry.getData().add(series1);
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
    public void initialize2() {
        // Đọc file + parse
        List<String> rawLogs = FileUtils.readLogFile("src/main/resources/data/mergeok.txt");
        List<IptablesModel> parsedLogs = IptablesLogProcessor.parseLogs(rawLogs);

        masterData.addAll(parsedLogs);

        // Setup columns
        prefixColumnIPT.setCellValueFactory(new PropertyValueFactory<>("logPrefix"));
        sourceIPColumnIPT.setCellValueFactory(new PropertyValueFactory<>("sourceIP"));
        destinationIPColumnIPT.setCellValueFactory(new PropertyValueFactory<>("destinationIP"));
        sourcePortColumnIPT.setCellValueFactory(new PropertyValueFactory<>("sourcePort"));
        destinationPortColumnIPT.setCellValueFactory(new PropertyValueFactory<>("destinationPort"));
        protocolColumnIPT.setCellValueFactory(new PropertyValueFactory<>("protocol"));
        lengthColumnIPT.setCellValueFactory(new PropertyValueFactory<>("length"));
        inInterfaceColumnIPT.setCellValueFactory(new PropertyValueFactory<>("inInterface"));
        outInterfaceColumnIPT.setCellValueFactory(new PropertyValueFactory<>("outInterface"));
        macColumnIPT.setCellValueFactory(new PropertyValueFactory<>("macAddress"));
        // timestampColumn: LocalDateTime => string
        timestampColumnIPT.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTimestamp() != null) {
                return new ReadOnlyObjectWrapper<>(
                        cellData.getValue().getTimestamp().toString()
                );
            } else {
                return new ReadOnlyObjectWrapper<>("null");
            }
        });

        logTableIP.setItems(masterData);

        // Cập nhật
        updateStatistics(masterData);
    }

    @FXML
    public void onSearchClickedIPT() {
        // Lấy from, to
        LocalDate fromLocal = fromDatePickerIPT.getValue();
        LocalDate toLocal = toDatePickerIPT.getValue();
        // Chuyển sang Date
        Date fromDate = (fromLocal != null) ? Date.valueOf(fromLocal) : null;
        Date toDate = (toLocal != null) ? Date.valueOf(toLocal) : null;

        String search = searchField.getText().trim().toLowerCase();

        // Filter
        List<IptablesModel> filtered = masterData.stream()
                .filter(e -> {
                    // 1) search
                    if (!search.isEmpty()) {
                        boolean match = false;
                        if (e.getLogPrefix() != null && e.getLogPrefix().toLowerCase().contains(search)) match = true;
                        if (!match && e.getSourceIP() != null && e.getSourceIP().toLowerCase().contains(search)) match = true;
                        if (!match && e.getProtocol() != null && e.getProtocol().toLowerCase().contains(search)) match = true;
                        // v.v. => destinationIP, in/outInterface
                        if (!match) return false;
                    }
                    // 2) time range
                    if (e.getTimestamp() != null) {
                        // convert to Date
                        java.util.Date d = java.util.Date.from(
                                e.getTimestamp().atZone(java.time.ZoneId.systemDefault()).toInstant()
                        );
                        if (fromDate != null && d.before(fromDate)) {
                            return false;
                        }
                        if (toDate != null && d.after(toDate)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        ObservableList<IptablesModel> filteredList = FXCollections.observableArrayList(filtered);
        logTableIP.setItems(filteredList);

        // Cập nhật chart
        updateStatistics(filteredList);
    }

    private void updateStatistics(List<IptablesModel> data) {
        int totalReq = LogAnalyzer.calculateTotalRequests(data);
        int blocked  = LogAnalyzer.calculateBlockedRequests(data);
        long size    = LogAnalyzer.calculateThroughput(data);

        totalRequestLabelIPT.setText(String.valueOf(totalReq));
        totalFailLabelIPT.setText(String.valueOf(blocked));
        totalSizeLabelIPT.setText(size + " bytes");

        updatePieChart(data);
        updateBarChart(data);
    }

    private void updatePieChart(List<IptablesModel> data) {
        long droppedCount = data.stream()
                .filter(e -> e.getLogPrefix() != null && e.getLogPrefix().toLowerCase().contains("dropped"))
                .count();
        long acceptCount = data.stream()
                .filter(e -> e.getLogPrefix() != null && e.getLogPrefix().toLowerCase().contains("accept"))
                .count();


        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Dropped", droppedCount),
                new PieChart.Data("Accept", acceptCount)

        );
        requestPieChartIPT.setData(pieData);
    }

    private void updateBarChart(List<IptablesModel> data) {
        logBarChartIPT.getData().clear();
        Map<String, Long> prefixCount = data.stream()
                .filter(e -> {
                    String p = e.getLogPrefix();
                    return p != null && !p.toLowerCase().contains("message repeated");
                })
                .collect(Collectors.groupingBy(
                        IptablesModel::getLogPrefix,
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Log Prefix Stats");

        for (var entry : prefixCount.entrySet()) {
            String prefix = entry.getKey();
            long count = entry.getValue();
            // cắt bớt if needed
            if (prefix.length() > 20) {
                prefix = prefix.substring(0, 20) + "...";
            }
            series.getData().add(new XYChart.Data<>(prefix, count));
        }

        logBarChartIPT.getData().add(series);
        // fix text xoay
        CategoryAxis xAxis = (CategoryAxis) logBarChartIPT.getXAxis();
        xAxis.setTickLabelRotation(0);
    }

    // ModsecController
    @FXML
    public void initialize1() {
        // Cài đặt các cột TreeTableView
        idColumn3.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn3.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn3.setCellValueFactory(new PropertyValueFactory<>("time"));
        ipColumn3.setCellValueFactory(new PropertyValueFactory<>("clientIp"));
        statusColumn3.setCellValueFactory(new PropertyValueFactory<>("status"));
        requestUriColumn3.setCellValueFactory(new PropertyValueFactory<>("requestUri"));
        userAgentColumn3.setCellValueFactory(new PropertyValueFactory<>("userAgent"));
        messageColumn3.setCellValueFactory(new PropertyValueFactory<>("message"));
        actionColumn3.setCellValueFactory(new PropertyValueFactory<>("action"));

        // Thiết lập StringConverter cho DatePicker
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

        // DatePicker fromDate
        fromDate3.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? date.format(dateFormatter) : "";  // Hiển thị dd/MM/yyyy
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty())
                        ? LocalDate.parse(string, dateFormatter)
                        : null;  // Chuyển đổi chuỗi thành LocalDate
            }
        });

        // DatePicker toDate
        toDate3.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? date.format(dateFormatter) : "";  // Hiển thị dd/MM/yyyy
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty())
                        ? LocalDate.parse(string, dateFormatter)
                        : null;  // Chuyển đổi chuỗi thành LocalDate
            }
        });
    }

    @FXML
    private void handleSearch() {
        String keyword = searchTextField3.getText();
        LocalDate from = fromDate3.getValue();  // Lấy giá trị từ DatePicker (LocalDate)
        LocalDate to = toDate3.getValue();      // Lấy giá trị từ DatePicker (LocalDate)

        // Nếu từ ngày và đến ngày không null, chuyển đổi chúng thành định dạng phù hợp
        String fromFormatted = from != null ? from.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH)) : "";
        String toFormatted = to != null ? to.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH)) : "";

        // Tìm kiếm với các tham số đã lấy
        ArrayList<LogEntry> results = searchEngine.SearchModsec(keyword, fromFormatted, toFormatted, "", "");
        ObservableList<LogEntry> filteredData = FXCollections.observableArrayList();
        // Đưa dữ liệu vào TreeTableView
        TreeItem<LogEntry> root = new TreeItem<>();
        for (LogEntry entry : results) {
            root.getChildren().add(new TreeItem<>(entry));
        }
        filteredData.clear();
//        for(int i = 0; i <= 2; i++){
//            filteredData.add(results.get(i));
//        }
        filteredData.addAll(results);
        logTable.setItems(filteredData);

        // Cập nhật biểu đồ
        updateCharts(results);
    }

    private void updateCharts(ArrayList<LogEntry> results) {
        // Cập nhật PieChart
        pieChartMod.getData().clear();
        long successCount = results.stream().filter(e -> "200".equals(e.getStatus())).count();
        long failCount = results.size() - successCount;

        pieChartMod.getData().addAll(
                new PieChart.Data("Success", successCount),
                new PieChart.Data("Fail", failCount)
        );

        // Cập nhật BarChart
        barChartMod.getData().clear();

        // Sử dụng TreeMap để tự động sắp xếp theo ngày
        var dataMap = new TreeMap<LocalDate, Long>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (LogEntry entry : results) {
            LocalDate date = LocalDate.parse(entry.getDate(), dateFormatter);
            dataMap.put(date, dataMap.getOrDefault(date, 0L) + 1);
        }

        // Tạo series mới
        var series = new javafx.scene.chart.XYChart.Series<String, Number>();
        for (var entry : dataMap.entrySet()) {
            String formattedDate = entry.getKey().format(dateFormatter); // Chuyển ngày lại thành chuỗi để hiển thị
            series.getData().add(new javafx.scene.chart.XYChart.Data<>(formattedDate, entry.getValue()));
        }

        // Thêm series vào BarChart
        barChartMod.getData().add(series);

    }

}
