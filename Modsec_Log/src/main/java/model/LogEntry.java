
package model;

public class LogEntry {
    private String id;
    private String date;

    private String time;
    private String clientIp;

    private String status;
    private String requestUri;
    private String userAgent;
    private String message;
    private String action;

    // Constructor
    public LogEntry(String id, String date, String time, String clientIp, String status, String requestUri, String userAgent, String message, String action) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.clientIp = clientIp;
        this.status = status;
        this.requestUri = requestUri;
        this.userAgent = userAgent;
        this.message = message;
        this.action = action;
    }

    // Getter methods
    public String getId() {
        return id;
    }


//    public String getTimestamp() {
//        return timestamp;
//    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }



    public String toString() {
        return "LogEntry{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", status='" + status +'\'' +
                ", requestUri='" + requestUri + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", message='" + message + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
//
//import java.io.*;
//import java.util.*;
//import java.util.regex.*;
//
//public class LogEntry {
//
//    //public static class LogEntry {
//        String timestamp;
//        String uniqueId;
//        String clientIp;
//        String requestUri;
//        String userAgent;
//        String responseStatus;
//        String errorMessage;
//        // Các thông tin khác bạn muốn lấy thêm
//
//        @Override
//        public String toString() {
//            return "Timestamp: " + timestamp + ", UniqueID: " + uniqueId +
//                    ", ClientIP: " + clientIp + ", URI: " + requestUri +
//                    ", UserAgent: " + userAgent + ", Status: " + responseStatus +
//                    ", ErrorMsg: " + errorMessage;
//        }
//    }
//
//    public static List<LogEntry> parseLogFile(String filePath) throws IOException {
//        List<LogEntry> entries = new ArrayList<>();
//        BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        String line;
//        LogEntry currentEntry = null;
//
//        while ((line = reader.readLine()) != null) {
//            if (line.startsWith("--")) {
//                // Start of new entry, save current one and create new one
//                if (currentEntry != null) {
//                    entries.add(currentEntry);
//                }
//                currentEntry = new LogEntry();
//            }
//
//            // Regular expression patterns for different log segments
//            if (line.contains("[") && line.contains("]")) {
//                // Extract timestamp and unique ID
//                Matcher timestampMatcher = Pattern.compile("\\[([0-9/]+:[0-9:]+[\\s+\\-]+[0-9]+)\\]").matcher(line);
//                if (timestampMatcher.find()) {
//                    currentEntry.timestamp = timestampMatcher.group(1);
//                }
//                Matcher uniqueIdMatcher = Pattern.compile("\\s([A-Za-z0-9]+)$").matcher(line);
//                if (uniqueIdMatcher.find()) {
//                    currentEntry.uniqueId = uniqueIdMatcher.group(1);
//                }
//            }
//            else if (line.startsWith("GET") || line.startsWith("POST")) {
//                // Extract URI and User-Agent
//                Matcher uriMatcher = Pattern.compile("(GET|POST)\\s(\\S+)").matcher(line);
//                if (uriMatcher.find()) {
//                    currentEntry.requestUri = uriMatcher.group(2);
//                }
//                if (line.contains("User-Agent:")) {
//                    Matcher userAgentMatcher = Pattern.compile("User-Agent: (.*)").matcher(line);
//                    if (userAgentMatcher.find()) {
//                        currentEntry.userAgent = userAgentMatcher.group(1);
//                    }
//                }
//            }
//            else if (line.startsWith("HTTP/1.1")) {
//                // Extract response status
//                Matcher statusMatcher = Pattern.compile("HTTP/1.1\\s(\\d{3})").matcher(line);
//                if (statusMatcher.find()) {
//                    currentEntry.responseStatus = statusMatcher.group(1);
//                }
//            }
//            else if (line.contains("Message:")) {
//                // Extract error message
//                Matcher errorMatcher = Pattern.compile("Message:\\s(.+)").matcher(line);
//                if (errorMatcher.find()) {
//                    currentEntry.errorMessage = errorMatcher.group(1);
//                }
//            }
//        }
//        if (currentEntry != null) {
//            entries.add(currentEntry); // Add last entry
//        }
//
//        reader.close();
//        return entries;
//    }
//
//    public static void main(String[] args) throws IOException {
//        String filePath = "path/to/your/logfile.log";
//        List<LogEntry> entries = parseLogFile(filePath);
//        for (LogEntry entry : entries) {
//            System.out.println(entry);
//        }
//    }
//}
