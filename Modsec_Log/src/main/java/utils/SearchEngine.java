package utils;

import model.LogEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class SearchEngine {
    public SearchEngine() {

    }

    public ArrayList<LogEntry> Search(String pat, String beginDate, String endDate, String beginTime, String endTime) {
        ArrayList<LogEntry> results = new ArrayList<>();
        int count = 0;
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
                            count++;
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

    private boolean matchesCriteria(LogEntry logEntry, String pat, String begin, String end, String beginTime, String endTime) {
        if (pat != null && !pat.isEmpty()) {
            // Kiểm tra từ khóa có xuất hiện trong một số trường của logEntry không
            if (!(logEntry.getRequestUri().contains(pat) || logEntry.getUserAgent().contains(pat) || logEntry.getMessage().contains(pat) || logEntry.getId().contains(pat) || logEntry.getClientIp().contains(pat) || logEntry.getAction().contains(pat) || logEntry.getStatus().contains(pat))) {
                return false;
            }
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (begin.isEmpty() && end.isEmpty()) {
            return true;
        } else {
            LocalDate beginDate = LocalDate.parse(begin, inputFormatter);
            LocalDate endDate = LocalDate.parse(end, inputFormatter);
            LocalDate logDate = LocalDate.parse(logEntry.getDate(), outputFormatter);
            if (!beginDate.toString().isEmpty()) {
                if (logDate.isBefore(beginDate)) {
                    return false;
                }
            }

            if (!endDate.toString().isEmpty()) {
                if (logDate.isAfter(endDate)) {
                    return false;
                }
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
        String dayDate = "";
        String monthDate = "";
        String yearDate = "";
        int monthDateNew = 0;
        String monthPart = "";

        if (logBlock.contains("[")) {
            try {
                // Lấy timestamp
                String timestamp = logBlock.split("\\[")[1].split("\\]")[0];
                String[] timestampParts = timestamp.split(":", 2);
                date = timestampParts[0];

                if (date.length() > 2) {
                    // Tách ngày, tháng, năm
                    String[] dateParts = date.split("/");
                    dayDate = dateParts[0];
                    monthDate = dateParts[1];
                    yearDate = dateParts[2];

                    // Xử lý tên tháng (chuyển sang viết hoa và lấy giá trị tháng)
                    Month month = Month.valueOf(monthDate.toUpperCase()); // Tháng phải viết hoa đầy đủ
                    monthDateNew = month.getValue(); // Lấy số tháng
                    if (monthDateNew > 0 && monthDateNew < 10) {
                        monthPart += "0" + monthDateNew;
                    }

                }


                date = dayDate + "/" + monthPart + "/" + yearDate;
                // time = timestampParts.length > 1 ? timestampParts[1] : "";
                if (timestampParts.length > 1) {
                    String[] timepart = timestampParts[1].split(" ", 2);
                    if (timepart.length > 1) {
                        time = timepart[0];
                    } else {
                        time = "";
                    }
                } else {
                    time = "";
                }
            } catch (Exception e) {
                // Xử lý lỗi nếu timestamp không hợp lệ
            }
        }

        // Request URI
        String requestUri = "";
        String[] URIpart = {"GET ", "POST ", "PUT ", "DELETE ", "HEAD ", "OPTIONS "};
        for (int i = 0; i < URIpart.length; i++) {
            if (logBlock.contains(URIpart[i])) {
                String field = URIpart[i];
                requestUri = logBlock.split(field)[1].split("\n", 2)[0];
            }
        }

        String status = "";
        String c = id + "-F--";
        if (logBlock.contains((c))) {
            status = logBlock.split(c)[1].split("\n")[1].split(" ")[1];
        }


        // User-Agent
        String userAgent = extractField(logBlock, "User-Agent:");

        // Message
        String message = extractField(logBlock, "Message:");

        // Action
        String action = extractField(logBlock, "Action:");

        // Tạo và trả về đối tượng LogEntry
        return new LogEntry(id, date, time, clientIp, status, requestUri, userAgent, message, action);
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