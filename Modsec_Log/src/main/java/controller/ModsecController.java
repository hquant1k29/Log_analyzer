package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.StringConverter;
import utils.SearchEngine;
import model.LogEntry;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ModsecController {

    @FXML
    private TextField searchTextField;  // TextField for the search keyword
    @FXML
    private DatePicker fromDate;  // DatePicker for the start date
    @FXML
    private DatePicker toDate;  // DatePicker for the end date
    @FXML
    private TreeTableView<LogEntry> logTreeTable;  // TreeTableView for displaying search results
    @FXML
    private TreeTableColumn<LogEntry, String> idColumn, dateColumn, timeColumn, ipColumn, statusColumn, requestUriColumn, userAgentColumn, messageColumn, actionColumn;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private PieChart pieChart;

    private final SearchEngine searchEngine = new SearchEngine();

    @FXML
    private void initialize() {
        // Cài đặt các cột TreeTableView
        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("time"));
        ipColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("clientIp"));
        statusColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("status"));
        requestUriColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("requestUri"));
        userAgentColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("userAgent"));
        messageColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("message"));
        actionColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("action"));

        // Thiết lập StringConverter cho DatePicker
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);

        // DatePicker fromDate
        fromDate.setConverter(new StringConverter<LocalDate>() {
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
        toDate.setConverter(new StringConverter<LocalDate>() {
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
        String keyword = searchTextField.getText();
        LocalDate from = fromDate.getValue();  // Lấy giá trị từ DatePicker (LocalDate)
        LocalDate to = toDate.getValue();      // Lấy giá trị từ DatePicker (LocalDate)

        // Nếu từ ngày và đến ngày không null, chuyển đổi chúng thành định dạng phù hợp
        String fromFormatted = from != null ? from.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH)) : "";
        String toFormatted = to != null ? to.format(DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH)) : "";

        // Tìm kiếm với các tham số đã lấy
        ArrayList<LogEntry> results = searchEngine.Search(keyword, fromFormatted, toFormatted, "", "");

        // Đưa dữ liệu vào TreeTableView
        TreeItem<LogEntry> root = new TreeItem<>();
        for (LogEntry entry : results) {
            root.getChildren().add(new TreeItem<>(entry));
        }
        logTreeTable.setRoot(root);
        logTreeTable.setShowRoot(false);

        // Cập nhật biểu đồ
        updateCharts(results);
    }

    private void updateCharts(ArrayList<LogEntry> results) {
        // Cập nhật PieChart
        pieChart.getData().clear();
        long successCount = results.stream().filter(e -> "200".equals(e.getStatus())).count();
        long failCount = results.size() - successCount;

        pieChart.getData().addAll(
                new PieChart.Data("Success", successCount),
                new PieChart.Data("Fail", failCount)
        );

        // Cập nhật BarChart
        barChart.getData().clear();
        var dataMap = new HashMap<String, Long>();
        for (LogEntry entry : results) {
            dataMap.put(entry.getDate(), dataMap.getOrDefault(entry.getDate(), 0L) + 1);
        }

        var series = new javafx.scene.chart.XYChart.Series<String, Number>();
        for (var entry : dataMap.entrySet()) {
            series.getData().add(new javafx.scene.chart.XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);
    }
}
