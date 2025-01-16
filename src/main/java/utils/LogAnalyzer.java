package utils;
import log.IPtableLog;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
public class LogAnalyzer {
    /**
     * Tính tổng số request (số dòng log) trong danh sách.
     */
    public static int calculateTotalRequests(List<IPtableLog> logEntries) {
        return (logEntries == null) ? 0 : logEntries.size();
    }

    /**
     * Tính tổng 'length' của tất cả gói tin (nếu null thì coi như 0).
     */
    public static long calculateThroughput(List<IPtableLog> logEntries) {
        long total = 0;
        if (logEntries == null) return 0;

        for (IPtableLog entry : logEntries) {
            if (entry.getLength() != null) {
                total += entry.getLength();
            }
        }
        return total;
    }

    /**
     * Tính số lượng request "bị chặn".
     * Ở iptables log, ta có thể dựa vào prefix "Dropped..." để coi là chặn.
     * Prefix chứa "Dropped" => blocked.
     */
    public static int calculateBlockedRequests(List<IPtableLog> logEntries) {
        if (logEntries == null) return 0;
        return (int) logEntries.stream()
                .filter(e -> {
                    String p = e.getLogPrefix();
                    return p != null && p.toLowerCase().contains("dropped");
                })
                .count();
    }

    /**
     * Tính số lượng request trong khoảng thời gian [startTime, endTime].
     * Chú ý chuyển LocalDateTime -> Date để xài TimeUtils.isWithinTimeRange
     */
    public static int calculateRequestsInTimeRange(List<IPtableLog> logEntries, Date startTime, Date endTime) {
        if (logEntries == null || startTime == null || endTime == null) return 0;

        int count = 0;
        for (IPtableLog entry : logEntries) {
            LocalDateTime ldt = entry.getTimestamp();
            if (ldt == null) continue;

            // Chuyển sang Date
            Date logDate = toDate(ldt);
            if (TimeUtils.isWithinTimeRange(logDate, startTime, endTime)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Chuyển LocalDateTime -> Date (zone mặc định)
     */
    private static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
