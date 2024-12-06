package application;

import model.LogEntry;
import utils.SearchEngine;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        Scanner sc = new Scanner(System.in);

        // 1. Lọc log theo trường
        System.out.println("Enter the field to filter logs (clientip, requesturi, useragent, action, message): ");
        String field = sc.nextLine();
        System.out.println("Enter the value: ");
        String value = sc.nextLine();
        System.out.println("Filtering logs based on a field:");
        ArrayList<LogEntry> filterLogs = searchEngine.searchByField(field, value);
        filterLogs.forEach(System.out::println);


        //2. Tính số request theo giờ
        System.out.println("Enter the start time (format: dd/MMM/yyyy:HH:mm:ss Z):");
        String startTime = sc.nextLine();
        System.out.println("Enter the end time (format: dd/MMM/yyyy:HH:mm:ss Z):");
        String endTime = sc.nextLine();

        System.out.println("\nCounting requests:");
        int requestCount = searchEngine.countRequestsInTimeRange(startTime,endTime);
        System.out.println("\nRequest count: " + requestCount);
        sc.close();
    }
}
