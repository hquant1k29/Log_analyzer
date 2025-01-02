package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import utils.searchEngine;
import log.LogEntry;
import javafx.collections.FXCollections;


public class ModSecController {

    @FXML
    private Button btnCalculateRequests;

    @FXML
    private Button btnFilterLogs;

    @FXML
    private Button btnTimeRange;

    @FXML
    private Button btnLoginChart;

    @FXML
    private Button btnExit;

    @FXML
    private Label statusLabel;

    @FXML
    private LineChart<Number, Number> loginChart;

    @FXML
    private TextField fieldInput; // Trường nhập liệu cho field
    @FXML
    private TextField valueInput; // Trường nhập liệu cho giá trị lọc

    @FXML
    private ListView<String> logListView; // ListView để hiển thị logs tìm được

    @FXML
    private VBox filterLogsSection; // Phần tử chứa trường lọc logs

    private final searchEngine searchEngine = new searchEngine();

    @FXML
    public void initialize() {
        // Thiết lập các sự kiện cho các nút
        btnCalculateRequests.setOnMouseClicked(this::handleCalculateRequests);
        btnFilterLogs.setOnMouseClicked(this::handleFilterLogs);
        btnTimeRange.setOnMouseClicked(this::handleTimeRange);
        btnLoginChart.setOnMouseClicked(this::handleLoginChart);
        btnExit.setOnMouseClicked(this::handleExit);
    }

    private void handleCalculateRequests(MouseEvent event) {
        int totalRequests = searchEngine.countTotalRequests();
        statusLabel.setText("Total Requests: " + totalRequests);
    }

    private void handleFilterLogs(MouseEvent event) {
        // Hiện phần nhập liệu và nút tìm kiếm cho chức năng lọc logs
        filterLogsSection.setVisible(true);
        statusLabel.setText("Enter the field and value to filter logs.");
        logListView.setVisible(false); // Ẩn danh sách logs trước khi tìm kiếm
    }

    private void handleTimeRange(MouseEvent event) {
        // Hiện phần nhập liệu và nút tìm kiếm cho chức năng tìm kiếm thời gian
        statusLabel.setText("Enter time range to count requests.");
    }

    private void handleLoginChart(MouseEvent event) {
        statusLabel.setText("Displaying login chart by hour.");
        String date = "2024-12-18"; // Bạn có thể lấy ngày từ input người dùng
        var hourlyLogins = searchEngine.analyzeLoginsByHour(date);

        // Tạo và hiển thị biểu đồ
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Login Activity");

        hourlyLogins.forEach((hour, count) -> {
            series.getData().add(new XYChart.Data<>(hour, count));
        });

        loginChart.getData().clear();
        loginChart.getData().add(series);
        loginChart.setVisible(true);
    }

    private void handleExit(MouseEvent event) {
        System.exit(0);
    }

    // Xử lý nút "Search" cho chức năng tìm kiếm logs

    @FXML
    private void handleSearchLogs() {
        String field = fieldInput.getText();
        String value = valueInput.getText();

        if (field.isEmpty() || value.isEmpty()) {
            statusLabel.setText("Please enter both field and value.");
            return;
        }

        // Lọc logs theo field và value
        var filteredLogs = searchEngine.searchByField(field, value);
        ObservableList<String> logItems = FXCollections.observableArrayList();

        // Chuyển các logs thành chuỗi và thêm vào danh sách
        for (LogEntry log : filteredLogs) {
            logItems.add(log.toString());  // Giả sử LogEntry có phương thức toString() phù hợp
        }

        // Cập nhật ListView với các item tìm được
        logListView.setItems(logItems);
        logListView.setVisible(true);  // Hiển thị ListView
        statusLabel.setText("Found " + filteredLogs.size() + " logs.");
    }

}
