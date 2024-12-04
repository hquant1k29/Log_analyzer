package model;

public class LogEntry {
    private String id;
    private String timestamp;
    private String clientIp;
    private String requestUri;
    private String userAgent;
    private String message;
    private String action;

    public LogEntry(String id, String timestamp, String clientIp, String requestUri, String userAgent, String message, String action) {
        this.id = id;
        this.timestamp = timestamp;
        this.clientIp = clientIp;
        this.requestUri = requestUri;
        this.userAgent = userAgent;
        this.message = message;
        this.action = action;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Timestamp: %s | Client IP: %s | Request URI: %s | User-Agent: %s | Message: %s | Action: %s",
                id, timestamp, clientIp, requestUri, userAgent, message, action);
    }
}
