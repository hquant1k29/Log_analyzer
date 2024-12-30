package log;

public class AccessLog {
    private String ip;
    private String log;
    private String userID;
    private String date;
    private String time;
    private String request;
    private int status;
    private int size;
    private String referer;
    private String userAgent;

    public AccessLog(String ip, String log, String userID, String date,String time, String request, int status, int size, String referer, String userAgent) {
        this.ip = ip;
        this.log = log;
        this.userID = userID;
        this.date = date;
        this.time = time;
        this.request = request;
        this.status = status;
        this.size = size;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
