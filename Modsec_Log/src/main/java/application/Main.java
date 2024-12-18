package application;

import chart.LoginChart;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.LogEntry;
import utils.SearchEngine;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main extends Application {

    // Các nút được ánh xạ từ FXML
    @FXML
    private Button btnCalculateRequests;
    @FXML
    private Button btnFilterLogs;
    @FXML
    private Button btnCountRequestsTime;
    @FXML
    private Button btnLoginChart;

    // Biểu đồ login activity
    @FXML
    private LineChart<Number, Number> loginChart;

    private SearchEngine searchEngine = new SearchEngine(); // Khởi tạo SearchEngine

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            // Đặt tên cho cửa sổ
            stage.setTitle("Log Analysis Application");

            // Load tệp FXML vào
            Parent root = FXMLLoader.load(getClass().getResource("/view.fxml"));

            // Tạo Scene và đặt vào Stage
            Scene scene = new Scene(root, 1280, 768);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xử lý sự kiện cho nút "Calculate Total Requests"
    @FXML
    private void handleCalculateRequests() {
        int totalRequests = searchEngine.countTotalRequests();
        System.out.println("Total Requests: " + totalRequests);
    }

    // Xử lý sự kiện cho nút "Filter Logs"
    @FXML
    private void handleFilterLogs() {
        // Lọc logs - code cần thực hiện
        System.out.println("Filtering logs...");
    }

    // Xử lý sự kiện cho nút "Count Requests in Time Range"
    @FXML
    private void handleCountRequestsTime() {
        // Đếm requests trong thời gian - code cần thực hiện
        System.out.println("Counting requests in time range...");
    }

    // Xử lý sự kiện cho nút "Generate Login Activity Chart"
    @FXML
    private void handleLoginChart() {
        // Lấy ngày từ người dùng (hoặc đặt giá trị ngày cố định)
        String date = "2024-12-18"; // Bạn có thể lấy ngày từ input người dùng
        Map<Integer, Integer> hourlyLogins = searchEngine.analyzeLoginsByHour(date);

        // Tạo và hiển thị biểu đồ
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Login Activity");

        // Thêm dữ liệu vào biểu đồ
        for (Map.Entry<Integer, Integer> entry : hourlyLogins.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        loginChart.getData().clear();  // Xóa dữ liệu cũ
        loginChart.getData().add(series);  // Thêm dữ liệu mới
        loginChart.setVisible(true);  // Hiển thị biểu đồ
    }

    // Hàm main chứa menu dòng lệnh (nếu cần)
    public void runConsoleMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Hiển thị menu lựa chọn
            System.out.println("\n--- Log Analysis Menu ---");
            System.out.println("1. Calculate total requests");
            System.out.println("2. Filter logs by field");
            System.out.println("3. Count requests in a time range");
            System.out.println("4. Display login activity chart by hour");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa dòng mới còn lại trong buffer

            switch (choice) {
                case 1 -> {
                    // Tính tổng số request
                    System.out.println("\nCalculating total requests...");
                    int totalRequests = searchEngine.countTotalRequests();
                    System.out.println("Total Requests: " + totalRequests);
                }
                case 2 -> {
                    // Lọc log theo trường
                    System.out.println("\nEnter the field to filter logs (clientip, requesturi, useragent, action, message): ");
                    String field = scanner.nextLine();

                    System.out.println("Enter the value to filter by: ");
                    String value = scanner.nextLine();

                    System.out.println("\nFiltering logs...");
                    ArrayList<LogEntry> filteredLogs = searchEngine.searchByField(field, value);
                    System.out.println("Filtered Logs:");
                    filteredLogs.forEach(System.out::println);
                }
                case 3 -> {
                    // Tính số request trong khoảng thời gian
                    System.out.println("\nEnter the start time (format: dd/MMM/yyyy:HH:mm:ss Z): ");
                    String startTime = scanner.nextLine();

                    System.out.println("Enter the end time (format: dd/MMM/yyyy:HH:mm:ss Z): ");
                    String endTime = scanner.nextLine();

                    System.out.println("\nCounting requests...");
                    int requestCount = searchEngine.countRequestsInTimeRange(startTime, endTime);
                    System.out.println("Request Count: " + requestCount);
                }
                case 4 -> {
                    System.out.println("choose day for analyze :");
                    String date = scanner.nextLine();

                    System.out.println("\nGenerating login activity chart by hour...");
                    Map<Integer, Integer> hourlyLogins = searchEngine.analyzeLoginsByHour(date);
                    LoginChart loginChart = new LoginChart();
                    loginChart.displayLoginChartByHour(hourlyLogins, date);
                }
                case 5 -> {
                    // Thoát chương trình
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Please select again.");
            }
        }
    }
}
