package utils;

import model.LogEntry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Lớp hỗ trợ tìm kiếm và phân tích log ModSecurity.
 */
public class SearchEngine {
    // Định dạng thời gian để phân tích log ModSecurity
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");

    /**
     * Tìm kiếm log theo trường và giá trị cụ thể.
     *
     * @param field Trường cần tìm kiếm (clientip, useragent, ...)
     * @param value Giá trị cần so khớp
     * @return Danh sách các log khớp với điều kiện
     */
    public ArrayList<LogEntry> searchByField(String field, String value) {
        ArrayList<LogEntry> results = new ArrayList<>(); // Danh sách kết quả
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.NewAnalyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder(); // Dùng để lưu block log hiện tại

            while ((line = br.readLine()) != null) {
                // Nếu gặp dòng đánh dấu block log mới
                if (line.startsWith("--") && line.endsWith("--")) {
                    // Phân tích block trước đó nếu có
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null && matchesField(logEntry, field, value)) {
                            results.add(logEntry);
                        }
                        currentBlock.setLength(0); // Xóa nội dung block hiện tại
                    }
                }
                currentBlock.append(line).append("\n"); // Thêm dòng vào block hiện tại
            }

            // Phân tích block log cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null && matchesField(logEntry, field, value)) {
                    results.add(logEntry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        return results; // Trả về danh sách kết quả
    }

    /**
     * Kiểm tra nếu log khớp với trường và giá trị cần tìm.
     *
     * @param logEntry Log cần kiểm tra
     * @param field Trường cần kiểm tra
     * @param value Giá trị cần so khớp
     * @return true nếu log khớp, ngược lại false
     */
    private boolean matchesField(LogEntry logEntry, String field, String value) {
        return switch (field.toLowerCase()) {
            case "clientip" -> logEntry.getClientIp().equals(value); // Lọc theo IP khách
            case "requesturi" -> logEntry.getRequestUri().contains(value); // Lọc theo URI yêu cầu
            case "useragent" -> logEntry.getUserAgent().contains(value); // Lọc theo User-Agent
            case "action" -> logEntry.getAction().equalsIgnoreCase(value); // Lọc theo hành động
            case "message" -> logEntry.getMessage().contains(value); // Lọc theo thông báo
            default -> false; // Trả về false nếu trường không hợp lệ
        };
    }

    /**
     * Tính số request trong một khoảng thời gian cụ thể.
     *
     * @param startTime Thời gian bắt đầu
     * @param endTime Thời gian kết thúc
     * @return Số lượng request trong khoảng thời gian
     */
    public int countRequestsInTimeRange(String startTime, String endTime) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.NewAnalyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();
            Date startDate = dateFormat.parse(startTime); // Phân tích thời gian bắt đầu
            Date endDate = dateFormat.parse(endTime); // Phân tích thời gian kết thúc

            while ((line = br.readLine()) != null) {
                if (line.startsWith("--") && line.endsWith("--")) {
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null && isInTimeRange(logEntry.getTimestamp(), startDate, endDate)) {
                            count++; // Tăng số đếm nếu log nằm trong khoảng thời gian
                        }
                        currentBlock.setLength(0); // Xóa nội dung block hiện tại
                    }
                }
                currentBlock.append(line).append("\n"); // Thêm dòng vào block hiện tại
            }

            // Kiểm tra block cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null && isInTimeRange(logEntry.getTimestamp(), startDate, endDate)) {
                    count++; // Tăng số đếm nếu log nằm trong khoảng thời gian
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error processing log file: " + e.getMessage());
        }
        return count; // Trả về số lượng request
    }

    /**
     * Kiểm tra nếu thời gian log nằm trong khoảng thời gian chỉ định.
     *
     * @param timestamp Thời gian của log
     * @param startDate Thời gian bắt đầu
     * @param endDate Thời gian kết thúc
     * @return true nếu thời gian log nằm trong khoảng, ngược lại false
     */
    private boolean isInTimeRange(String timestamp, Date startDate, Date endDate) {
        try {
            Date logDate = dateFormat.parse(timestamp); // Phân tích thời gian log
            return logDate.after(startDate) && logDate.before(endDate); // Kiểm tra nếu nằm trong khoảng
        } catch (ParseException e) {
            return false; // Trả về false nếu không thể phân tích thời gian
        }
    }
    /**
     * Đếm tổng số request trong file log.
     *
     * @return Tổng số request.
     */
    public int countTotalRequests() {
        int count = 0; // Biến đếm tổng số request
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.NewAnalyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder(); // Lưu block log hiện tại

            while ((line = br.readLine()) != null) {
                // Khi gặp block mới, kiểm tra block trước đó
                if (line.startsWith("--") && line.endsWith("--")) {
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null) {
                            count++; // Tăng biến đếm nếu có log hợp lệ
                        }
                        currentBlock.setLength(0); // Xóa nội dung block hiện tại
                    }
                }
                currentBlock.append(line).append("\n");
            }

            // Kiểm tra block cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null) {
                    count++; // Tăng biến đếm nếu có log hợp lệ
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        return count; // Trả về tổng số request
    }
    /**
     * Đếm số lượng đăng nhập theo giờ trong một ngày cụ thể.
     *
     * @param date Ngày cần phân tích (định dạng: dd/MMM/yyyy, ví dụ: 01/May/2018).
     * @return Map chứa giờ (0-23) và số lượng đăng nhập trong từng giờ.
     */
    public Map<Integer, Integer> analyzeLoginsByHour(String date) {
        Map<Integer, Integer> hourlyLogins = new TreeMap<>(); // TreeMap để sắp xếp theo giờ
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\0.NewAnalyze\\Log_analyzer\\Modsec_Log\\src\\main\\resources\\modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();

            while ((line = br.readLine()) != null) {
                // Khi gặp block mới, kiểm tra block trước đó
                if (line.startsWith("--") && line.endsWith("--")) {
                    if (currentBlock.length() > 0) {
                        LogEntry logEntry = parseLogEntry(currentBlock.toString());
                        if (logEntry != null && logEntry.getRequestUri().contains("login")) {
                            String timestamp = logEntry.getTimestamp();
                            if (timestamp.startsWith(date)) { // Kiểm tra ngày trùng khớp
                                int hour = Integer.parseInt(timestamp.substring(12, 14)); // Lấy giờ
                                hourlyLogins.put(hour, hourlyLogins.getOrDefault(hour, 0) + 1);
                            }
                        }
                        currentBlock.setLength(0);
                    }
                }
                currentBlock.append(line).append("\n");
            }

            // Xử lý block cuối cùng
            if (currentBlock.length() > 0) {
                LogEntry logEntry = parseLogEntry(currentBlock.toString());
                if (logEntry != null && logEntry.getRequestUri().contains("login")) {
                    String timestamp = logEntry.getTimestamp();
                    if (timestamp.startsWith(date)) {
                        int hour = Integer.parseInt(timestamp.substring(12, 14));
                        hourlyLogins.put(hour, hourlyLogins.getOrDefault(hour, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        return hourlyLogins; // Trả về Map chứa giờ và số lượng đăng nhập
    }




    /**
     * Phân tích một block log thành đối tượng LogEntry.
     *
     * @param logBlock Block log cần phân tích
     * @return Đối tượng LogEntry hoặc null nếu không phân tích được
     */
    private LogEntry parseLogEntry(String logBlock) {
        // Tách các dòng trong block log
        String[] lines = logBlock.split("\n");


        if (lines.length < 1) {
            return null; // Trả về null nếu block log trống
        }
        String id = lines[0];
        String clientIp = "Unknown";
        if(lines.length>=2){
        // Phân tích các trường trong log
            id = lines[1];
            if(id.contains("[")) {

                System.out.println(id);
                String[] idPart = id.split(" ");
                clientIp = idPart[3] ;
                System.out.println(clientIp);
            }
        }

        String timestamp = logBlock.contains("[") ? logBlock.split("\\[")[1].split("\\]")[0] : "Unknown";
        String requestUri = "Unknown";
        String userAgent = "Unknown";
        String message = "Unknown";
        String action = "Unknown";

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
}
