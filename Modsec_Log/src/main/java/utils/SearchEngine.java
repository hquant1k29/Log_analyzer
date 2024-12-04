package utils;

import model.LogEntry;
import service.StringCompare;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SearchEngine {
    public ArrayList<LogEntry> search(String pattern) {
        ArrayList<LogEntry> results = new ArrayList<>();
        StringCompare stringCompare = new StringCompare();

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.Analyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("--") && line.endsWith("--")) {
                    if (currentBlock.length() > 0) {
                        if (stringCompare.BoyerMoore(currentBlock.toString(), pattern) == 1) {
                            results.add(parseLogEntry(currentBlock.toString()));
                        }
                        currentBlock.setLength(0);
                    }
                }
                currentBlock.append(line).append("\n");
            }
            if (currentBlock.length() > 0 && stringCompare.BoyerMoore(currentBlock.toString(), pattern) == 1) {
                results.add(parseLogEntry(currentBlock.toString()));
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }

        return results;
    }

    private LogEntry parseLogEntry(String logBlock) {
        String id = logBlock.split("\n")[0].trim();
        String timestamp = logBlock.contains("[") ? logBlock.split("\\[")[1].split("]")[0] : "Unknown";
        String clientIp = logBlock.split(" ")[2];
        String requestUri = logBlock.contains("GET") ? logBlock.split("GET")[1].split(" ")[1] : "Unknown";
        String userAgent = logBlock.contains("User-Agent:") ? logBlock.split("User-Agent:")[1].split("\n")[0].trim() : "Unknown";
        String message = logBlock.contains("Message:") ? logBlock.split("Message:")[1].split("\n")[0].trim() : "Unknown";
        String action = logBlock.contains("Action:") ? logBlock.split("Action:")[1].split("\n")[0].trim() : "Unknown";

        return new LogEntry(id, timestamp, clientIp, requestUri, userAgent, message, action);
    }
}
