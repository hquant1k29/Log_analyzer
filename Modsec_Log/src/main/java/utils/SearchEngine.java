
package utils;
import model.LogEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.time.format.DateTimeFormatter;
import java.util.*;
//import java.util.regex.Pattern;

public class SearchEngine {
//    private int total = 0;
//    private int totalFail = 0;
//    private int tmpSize = 0;
//    private double totalSize = 0;
//    private HashMap<String, Integer> timeMap;
//    private HashMap<String, Integer> ipMap;
//    private HashMap<String, Integer> proto;
//    private int[] statusMap;

    public SearchEngine() {

    }

    public ArrayList<LogEntry> Search(String pat, String beginDate, String endDate, String beginTime, String endTime) {
        ArrayList<LogEntry> results = new ArrayList<>();
        int count =0;
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.NewAnalyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("--") && line.contains("-A--")) {
                    // Nếu đã có block log trước đó, phân tích và kiểm tra
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null && matchesCriteria(logEntry, pat, beginDate, endDate, beginTime, endTime)) {

                            results.add(logEntry);
                            count ++;
                        }
                        currentBlock.setLength(0); // Reset block
                    }
                }
                currentBlock.append(line).append("\n");
            }

            // Xử lý block cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null && matchesCriteria(logEntry, pat, beginDate, endDate, beginTime, endTime)) {
                    results.add(logEntry);
                    count++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        System.out.println("Number of matching results: " + count); // Hiển thị số kết quả
        return results;
    }

private boolean matchesCriteria(LogEntry logEntry, String pat, String beginDate, String endDate, String beginTime, String endTime) {
    if (pat != null && !pat.isEmpty()) {
        // Kiểm tra từ khóa có xuất hiện trong một số trường của logEntry không
        if (!(logEntry.getRequestUri().contains(pat)  ||
                logEntry.getUserAgent().contains(pat) ||
                logEntry.getMessage().contains(pat)   ||
                logEntry.getId().contains(pat)        ||
                logEntry.getClientIp().contains(pat)  ||
                logEntry.getAction().contains(pat))) {
            return false;
        }
    }

    if (beginDate != null && !beginDate.isEmpty()) {
        if (logEntry.getDate().compareTo(beginDate) < 0) {
            return false;
        }
    }

    if (endDate != null && !endDate.isEmpty()) {
        if (logEntry.getDate().compareTo(endDate) > 0) {
            return false;
        }
    }

    if (beginTime != null && !beginTime.isEmpty()) {
        if (logEntry.getTime().compareTo(beginTime) < 0) {
            return false;
        }
    }

    if (endTime != null && !endTime.isEmpty()) {
        if (logEntry.getTime().compareTo(endTime) > 0) {
            return false;
        }
    }

    return true;
}

    private LogEntry parseLogEntry(String logBlock) {
    if (logBlock == null || logBlock.isEmpty()) {
        return null; // Trả về null nếu block log trống
    }

    String[] lines = logBlock.split("\n");

    // ID và Client IP
    String id = "Unknown";
    String clientIp = "Unknown";
    if (lines.length >= 1) {
        String[] idParts = lines[0].split("-");
        if (idParts.length >= 3) {
            id = idParts[2].trim();
        }
    }
    if (lines.length >= 2) {
        String[] parts = lines[1].split(" ");
        if (parts.length >= 4) {
            clientIp = parts[3].trim();
        }
    }

    // Timestamp
    String date = "Unknown";
    String time = "Unknown";
    if (logBlock.contains("[")) {
        try {
            String timestamp = logBlock.split("\\[")[1].split("\\]")[0];
            String[] timestampParts = timestamp.split(":", 2);
            date = timestampParts[0];
           // time = timestampParts.length > 1 ? timestampParts[1] : "";
            if(timestampParts.length>1){
                String []timepart = timestampParts[1].split(" ",2);
                if(timepart.length>1){
                    time = timepart[0];
                }
                else{
                    time = "";
                }
            }
            else{
                time = "";
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu timestamp không hợp lệ
        }
    }

    // Request URI
    String requestUri = "";
    String []URIpart= {"GET ","POST ","PUT ","DELETE ","HEAD ","OPTIONS "};
    for(int i = 0 ; i<URIpart.length;i++){
        if (logBlock.contains(URIpart[i])) {
                String field = URIpart[i];
                requestUri = logBlock.split(field)[1].split("\n",2)[0];
        }
    }



    // User-Agent
    String userAgent = extractField(logBlock, "User-Agent:");

    // Message
    String message = extractField(logBlock, "Message:");

    // Action
    String action = extractField(logBlock, "Action:");

    // Tạo và trả về đối tượng LogEntry
    return new LogEntry(id, date, time, clientIp, requestUri, userAgent, message, action);
}

    // Phương thức hỗ trợ để trích xuất giá trị từ log
    private String extractField(String logBlock, String field) {
        if (logBlock.contains(field)) {
            try {
                return logBlock.split(field)[1].split("\n")[0].trim();
            } catch (Exception e) {
                return "Unknown";
            }
        }
        return "Unknown";
    }




}