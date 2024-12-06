package utils;

import model.LogEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchEngine {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");

    // Phương thức để tìm kiếm log theo trường
    public ArrayList<LogEntry> searchByField(String field, String value) {
        ArrayList<LogEntry> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.Analyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();

            while ((line = br.readLine()) != null) {
                // Phân tích log khi bắt đầu block mới
                if (line.startsWith("--") && line.endsWith("--")) {
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null && matchesField(logEntry, field, value)) {
                            results.add(logEntry);
                        }
                        currentBlock.setLength(0);
                    }
                }
                currentBlock.append(line).append("\n");
            }

            // Phân tích log cho block cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null && matchesField(logEntry, field, value)) {
                    results.add(logEntry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        return results;
    }

    // Kiểm tra nếu trường trong log có khớp với giá trị
    private boolean matchesField(LogEntry logEntry, String field, String value) {
        return switch (field.toLowerCase()) {
            case "clientip" -> logEntry.getClientIp().equals(value);
            case "requesturi" -> logEntry.getRequestUri().contains(value);
            case "useragent" -> logEntry.getUserAgent().contains(value);
            case "action" -> logEntry.getAction().equalsIgnoreCase(value);
            case "message" -> logEntry.getMessage().contains(value);
            default -> false;
        };
    }

    // Phân tích log entry từ block log
    private LogEntry parseLogEntry(String logBlock) {
        String[] lines = logBlock.split("\n");

        if (lines.length < 1) {
            return null;
        }

        String id = lines[0].trim();
        String timestamp = "Unknown";
        String clientIp = "Unknown";
        String requestUri = "Unknown";
        String userAgent = "Unknown";
        String message = "Unknown";
        String action = "Unknown";

        if (logBlock.contains("[")) {
            timestamp = logBlock.split("\\[")[1].split("\\]")[0];
        }

        String[] fields = logBlock.split(" ");
        if (fields.length > 2) {
            clientIp = fields[2];
        }

        if (logBlock.contains("GET") || logBlock.contains("POST")) {
            requestUri = logBlock.split(" ")[1];
        }

        if (logBlock.contains("User-Agent:")) {
            userAgent = logBlock.split("User-Agent:")[1].split("\n")[0].trim();
        }

        if (logBlock.contains("Message:")) {
            message = logBlock.split("Message:")[1].split("\n")[0].trim();
        }

        if (logBlock.contains("Action:")) {
            action = logBlock.split("Action:")[1].split("\n")[0].trim();
        }

        return new LogEntry(id, timestamp, clientIp, requestUri, userAgent, message, action);
    }

    // Phương thức để đếm số request trong khoảng thời gian
    public int countRequestsInTimeRange(String startTime, String endTime) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);

            while ((line = br.readLine()) != null) {
                if (line.startsWith("--") && line.endsWith("--")) {
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null && isInTimeRange(logEntry.getTimestamp(), startDate, endDate)) {
                            count++;
                        }
                        currentBlock.setLength(0);
                    }
                }
                currentBlock.append(line).append("\n");
            }

            // Kiểm tra log cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null && isInTimeRange(logEntry.getTimestamp(), startDate, endDate)) {
                    count++;
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error processing log file: " + e.getMessage());
        }
        return count;
    }

    // Kiểm tra nếu thời gian của log nằm trong khoảng
    private boolean isInTimeRange(String timestamp, Date startDate, Date endDate) {
        try {
            Date logDate = dateFormat.parse(timestamp);
            return logDate.after(startDate) && logDate.before(endDate);
        } catch (ParseException e) {
            return false;
        }
    }
}
