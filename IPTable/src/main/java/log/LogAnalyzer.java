// Phân tích log
package log;

import models.LogEntry;
import utils.TimeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogAnalyzer {

    /**
     * Tính tổng số request trong danh sách log.
     *
     * @param logEntries Danh sách các log đã phân tích.
     * @return Tổng số request.
     */
    public static int calculateTotalRequests(List<LogEntry> logEntries) {
        return logEntries.size();
    }

    /**
     * Tính thông lượng (tổng chiều dài của tất cả gói tin) từ trường LEN.
     *
     * @param logEntries Danh sách các log đã phân tích.
     * @return Tổng thông lượng (bytes).
     */
    public static int calculateThroughput(List<LogEntry> logEntries) {
        int totalLength = 0;
        for (LogEntry entry : logEntries) {
            totalLength += entry.getLength();
        }
        return totalLength;
    }

    /**
     * Tính số lượng request trong một khung thời gian cụ thể.
     *
     * @param logEntries Danh sách các log đã phân tích.
     * @param startTime  Thời gian bắt đầu.
     * @param endTime    Thời gian kết thúc.
     * @return Số lượng request trong khung thời gian.
     */
    public static int calculateRequestsInTimeRange(List<LogEntry> logEntries, Date startTime, Date endTime) {
        int count = 0;
        for (LogEntry entry : logEntries) {
            if (TimeUtils.isWithinTimeRange(entry.getTimestamp(), startTime, endTime)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Tính số lượng request theo loại log (INPUT hoặc OUTPUT).
     *
     * @param logEntries Danh sách các log đã phân tích.
     * @param logType    Loại log cần đếm (INPUT hoặc OUTPUT).
     * @return Số lượng request thuộc loại log.
     */
    public static int calculateRequestsByType(List<LogEntry> logEntries, String logType) {
        int count = 0;
        for (LogEntry entry : logEntries) {
            if (logType.equalsIgnoreCase(entry.getLogType())) {
                count++;
            }
        }
        return count;
    }
// Phục vụ cho vẽ biểu đồ
    /**
     * Tính số request trong các khoảng thời gian gần nhất (12h, 24h, ...).
     *
     * @param logEntries Danh sách các log đã phân tích.
     * @param hours      Số giờ (ví dụ: 12, 24, 48).
     * @return Map với key là khoảng thời gian và value là số lượng request.
     */
    public static Map<String, Integer> calculateRequestsByTimeRanges(List<LogEntry> logEntries, int hours) {
        Map<String, Integer> timeRanges = new HashMap<>();

        Date now = new Date(); // Thời gian hiện tại
        for (int i = 1; i <= hours / 12; i++) {
            Date endTime = new Date(now.getTime() - (i - 1) * 12 * 60 * 60 * 1000L);
            Date startTime = new Date(now.getTime() - i * 12 * 60 * 60 * 1000L);

            int count = calculateRequestsInTimeRange(logEntries, startTime, endTime);
            timeRanges.put(i * 12 + " giờ trước", count);
        }

        return timeRanges;
    }

    /**
     * Đếm tần suất request theo từng địa chỉ IP.
     *
     * @param logEntries Danh sách các log đã phân tích.
     * @param hours      Số giờ (ví dụ: 12, 24, 48).
     * @return Map với key là địa chỉ IP và value là số lần request.
     */
    public static Map<String, Integer> calculateIPFrequency(List<LogEntry> logEntries, int hours) {
        Map<String, Integer> ipFrequency = new HashMap<>();

        Date now = new Date(); // Thời gian hiện tại
        Date startTime = new Date(now.getTime() - hours * 60 * 60 * 1000L);

        for (LogEntry entry : logEntries) {
            if (entry.getTimestamp().after(startTime)) {
                String sourceIP = entry.getSourceIP();
                ipFrequency.put(sourceIP, ipFrequency.getOrDefault(sourceIP, 0) + 1);
            }
        }

        return ipFrequency;
    }
}
