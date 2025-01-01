package application;

import charts.ChartGenerator;
import log.IptablesLogProcessor;
import log.LogAnalyzer;
import models.LogEntry;
import utils.FileUtils;
import utils.TimeUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String logFilePath = "src/main/resources/newinput_log.txt";

        List<String> rawLogs = FileUtils.readLogFile(logFilePath);

        if (rawLogs.isEmpty()) {
            System.out.println("File log không có dữ liệu hoặc không tồn tại.");
            return;
        }

        List<LogEntry> logEntries = IptablesLogProcessor.parseLogs(rawLogs);

        while (true) {
            System.out.println("\nChọn một tùy chọn:");
            System.out.println("1. Tra cứu theo IP");
            System.out.println("2. Phân tích tổng số request");
            System.out.println("3. Tính thông lượng");
            System.out.println("4. Tính số lượng request trong khoảng thời gian");
            System.out.println("5. Đếm số lượng request theo loại (INPUT/OUTPUT)");
            System.out.println("6. Số request theo khoảng thời gian gần nhất (biểu đồ cột)");
            System.out.println("7. Tần suất request theo IP (biểu đồ tròn)");
            System.out.println("8. Thoát");

            System.out.print("Nhập lựa chọn của bạn: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: // Tra cứu theo IP
                    System.out.print("Nhập IP cần tra cứu: ");
                    String searchIP = scanner.nextLine();
                    List<LogEntry> searchResults = IptablesLogProcessor.searchLogs(logEntries, searchIP);
                    System.out.println("Tìm thấy " + searchResults.size() + " log khớp với IP " + searchIP);
                    break;

                case 2: // Tổng số request
                    int totalRequests = LogAnalyzer.calculateTotalRequests(logEntries);
                    System.out.println("Tổng số request: " + totalRequests);
                    break;

                case 3: // Thông lượng
                    int throughput = LogAnalyzer.calculateThroughput(logEntries);
                    System.out.println("Thông lượng: " + throughput + " bytes");
                    break;

                case 4: // Request trong khoảng thời gian
                    System.out.print("Nhập thời gian bắt đầu (vd: Thg 11 21 23:50:00): ");
                    String startTimeStr = scanner.nextLine();
                    System.out.print("Nhập thời gian kết thúc (vd: Thg 11 21 23:55:00): ");
                    String endTimeStr = scanner.nextLine();

                    Date startTime = TimeUtils.parseTimestamp(startTimeStr);
                    Date endTime = TimeUtils.parseTimestamp(endTimeStr);

                    if (startTime != null && endTime != null) {
                        int requestsInRange = LogAnalyzer.calculateRequestsInTimeRange(logEntries, startTime, endTime);
                        System.out.println("Số request trong khoảng thời gian: " + requestsInRange);
                    } else {
                        System.out.println("Thời gian không hợp lệ. Vui lòng thử lại.");
                    }
                    break;

                case 6: // Số request theo thời gian gần nhất (Biểu đồ cột)
                    System.out.print("Nhập số giờ (12, 24, 48, ...): ");
                    int hours = scanner.nextInt();
                    Map<String, Integer> timeRanges = LogAnalyzer.calculateRequestsByTimeRanges(logEntries, hours);
                    ChartGenerator.generateBarChartForTimeRanges("Requests by Time Range", timeRanges);
                    break;

                case 7: // Tần suất request theo IP (biểu đồ tròn)
                    System.out.print("Nhập số giờ (12, 24, 48, ...): ");
                    int hoursForIP = scanner.nextInt();
                    Map<String, Integer> ipFrequency = LogAnalyzer.calculateIPFrequency(logEntries, hoursForIP);
                    ChartGenerator.generatePieChartForIPFrequency("IP Frequency Distribution", ipFrequency);
                    break;

                case 8: // Thoát
                    System.out.println("Thoát chương trình.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }
}
