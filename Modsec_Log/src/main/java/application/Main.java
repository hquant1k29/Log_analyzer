package application;

import model.LogEntry;
import utils.SearchEngine;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        String keyword = "/phpmyadmin"; // Tìm kiếm log liên quan đến "/phpmyadmin"

        ArrayList<LogEntry> results = searchEngine.search(keyword);
        System.out.println("Search Results:");
        for (LogEntry logEntry : results) {
            System.out.println(logEntry);
        }
    }
}
