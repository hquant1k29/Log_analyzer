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
                case 4  -> {
                    System.out.println("choose day for analyze");
                    String focusDay = scanner.nextLine();
                    System.out.println("\nDisplay login activity chart by hour");

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
