package log;

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

