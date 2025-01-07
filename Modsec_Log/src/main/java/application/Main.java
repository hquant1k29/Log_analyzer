package application;

import model.LogEntry;
import utils.SearchEngine;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== MENU =====");
            System.out.println("1. Tìm kiếm log");
            System.out.println("2. Thoát");
            System.out.print("Chọn một tùy chọn: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Xóa ký tự xuống dòng còn sót lại trong buffer
            String logFilePath = "D:\\0.mauthu\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt";
            switch (choice) {
                case 1:
                    System.out.println("Nhập từ khóa (hoặc để trống): ");
                    String keyword = scanner.nextLine().trim();
                    if (keyword.isEmpty()) keyword = null;

                    System.out.println("Nhập ngày bắt đầu (định dạng dd/MMM/yyyy, hoặc để trống): ");
                    String beginDate = scanner.nextLine().trim();
                    if (beginDate.isEmpty()) beginDate = null;

                    System.out.println("Nhập ngày kết thúc (định dạng dd/MMM/yyyy, hoặc để trống): ");
                    String endDate = scanner.nextLine().trim();
                    if (endDate.isEmpty()) endDate = null;

                    System.out.println("Nhập giờ bắt đầu (định dạng HH:mm:ss, hoặc để trống): ");
                    String beginTime = scanner.nextLine().trim();
                    if (beginTime.isEmpty()) beginTime = null;

                    System.out.println("Nhập giờ kết thúc (định dạng HH:mm:ss, hoặc để trống): ");
                    String endTime = scanner.nextLine().trim();
                    if (endTime.isEmpty()) endTime = null;
                    System.out.println("Từ khóa: " + keyword);
                    System.out.println("Ngày bắt đầu: " + beginDate);
                    System.out.println("Ngày kết thúc: " + endDate);
                    System.out.println("Giờ bắt đầu: " + beginTime);
                    System.out.println("Giờ kết thúc: " + endTime);

                    ArrayList<LogEntry> results = searchEngine.Search(keyword, beginDate, endDate, beginTime, endTime);

                    if (results.isEmpty()) {
                        System.out.println("Không tìm thấy log nào phù hợp.");
                    } else {
                        System.out.println("Kết quả tìm kiếm:");
                        for (LogEntry log : results) {
                            System.out.println(log);
                        }
                    }
                    break;

                case 2:
                    System.out.println("Chương trình kết thúc. Tạm biệt!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }
}
