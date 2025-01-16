package utils;
import log.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import log.ModsecLog;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Locale;

public class searchEngine implements Comparator<AccessLog> {

    private int totalFailMoc = 0;
    private int totalRequestMocsec;
    private int total = 0;
    private int totalFail = 0;
    private int tmpSize = 0;
    private double totalSize = 0;
    private HashMap<String,Integer> timeMap;
    private HashMap<String,Integer> ipMap;
    private HashMap<String,Integer> proto;
    private int [] statusMap;
    public searchEngine(){

    }

    public ArrayList<AccessLog> Search(String pat, String beginDate , String endDate, String beginTime, String endTime){

        ArrayList<AccessLog> list = new ArrayList<>();
        timeMap = new HashMap<>();
        ipMap = new HashMap<>();
        proto = new HashMap<>();
        statusMap = new int[600];
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //String fileName = "src/main/resources/data/apache_logs.txt";
        String fileName = "/var/log/apache2/access.log";
        //String fileName = "Apache_Log/src/main/resources/data/access1.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            stringCompare s = new stringCompare();
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\[([^]]*)]|\\S+"); // regrex split string to groups

            while ((line = br.readLine()) != null) {
                if (s.BoyerMoore(line, pat) == 1) {
                    // Split line to fields
                    ArrayList<String> temp = getStrings(pattern, line);
                    // handle format date,time
                    int temp5 = 0, temp6 = 0;
                    String date = "", time = "";
                    try {
                        try {
                            temp5 = Integer.parseInt(temp.get(5));
                            temp6 = Integer.parseInt(temp.get(6));
                        } catch (NumberFormatException e) {
                            temp5 = 0;
                            temp6 = 0;
                        }
                        String dateTime = temp.get(3);
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
                        // Định dạng output mong muốn
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        // Phân tích và định dạng lại thời gian
                        LocalDateTime unFormatDateTime = LocalDateTime.parse(dateTime, inputFormatter.withZone(ZoneOffset.UTC));
                        String formattedTime = unFormatDateTime.format(outputFormatter);
                        date = formattedTime.substring(0, 10);
                        time = formattedTime.substring(11, 19);


                    } catch (StringIndexOutOfBoundsException e) {
                        System.err.println("Error reading file: " + e.getMessage());
                    }
                    if(CheckInRange(date,beginDate,endDate) == 1 && CheckInRange(time.substring(0,5),beginTime,endTime) == 1){
                        AccessLog a = new AccessLog(temp.get(0), temp.get(1), temp.get(2), date,time, temp.get(4), temp5, temp6, temp.get(7), temp.get(8));
                        list.add(a);
                        String[] httpPro = temp.get(4).split("\\s+");
                        try {
                            total ++;
                            if(temp5 >= 400){
                                totalFail ++;
                            }
                            timeMap.merge(date,1,Integer::sum);
                            timeMap.merge(date + time.substring(0,2),1,Integer::sum);
                            timeMap.merge(date + time.substring(0,5),1,Integer::sum);
                            statusMap[temp5] ++;
                            ipMap.merge(temp.getFirst(),1,Integer::sum);
                            proto.merge(httpPro[0],1,Integer::sum);
                        }catch (Exception e) {
                            System.err.println("Error in here: " + e.getMessage());
                        }
                    } else if(CheckInRange(date,beginDate,endDate) == 2){
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return list;
    }

    private ArrayList<String> getStrings(Pattern pattern, String line) {
        ArrayList<String> temp = new ArrayList<>();
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // word in ""
                temp.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // word in []
                temp.add(matcher.group(2));
            } else {
                // word by space
                temp.add(matcher.group());
            }
        }
        return temp;
    }

    public int compare(AccessLog a, AccessLog b) {
        return a.getDate().compareTo(b.getDate());
    }
    public int CheckInRange(String time,String begin,String end){
        if (!Objects.equals(begin, "") && !Objects.equals(end, "")) {
            if(time.compareTo(begin) >= 0 && time.compareTo(end) <= 0) return 1;
            else if(time.compareTo(end) >= 0) return 2;
            else return 0;
        }
        else if (Objects.equals(begin, "") && !Objects.equals(end, "")){
            if(time.compareTo(end) <= 0) {
                return 1;
            }
            else return 2;
        }
        else if (!Objects.equals(begin, "") && Objects.equals(end, "")){
            if(time.compareTo(begin) >= 0) return 1;
            else return 0;
        }
        else if (Objects.equals(begin, "") && Objects.equals(end, "")){
            return 1;
        }
        return 0;
    }

    public ArrayList<ModsecLog> SearchModsec(String pat, String beginDate, String endDate, String beginTime, String endTime) {
        ArrayList<ModsecLog> results = new ArrayList<>();
        int count = 0;
        totalFailMoc = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/data/modsec_logs.txt"))) {
            String line;
            StringBuilder currentBlock = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.startsWith("--") && line.contains("-A--")) {
                    // Nếu đã có block log trước đó, phân tích và kiểm tra
                    if (!currentBlock.isEmpty()) {
                        ModsecLog modsecLog = parseLogEntry(currentBlock.toString());
                        if (modsecLog != null && matchesCriteria(modsecLog, pat, beginDate, endDate, beginTime, endTime)) {

                            results.add(modsecLog);
                            count++;
                            if(Integer.parseInt(modsecLog.getStatus()) >= 400){
                                totalFailMoc ++;
                            }
                        }
                        currentBlock.setLength(0); // Reset block
                    }
                }
                currentBlock.append(line).append("\n");
            }

            // Xử lý block cuối cùng
            if (!currentBlock.isEmpty()) {
                ModsecLog modsecLog = parseLogEntry(currentBlock.toString());
                if (modsecLog != null && matchesCriteria(modsecLog, pat, beginDate, endDate, beginTime, endTime)) {
                    results.add(modsecLog);
                    count++;
                    totalFailMoc++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        totalRequestMocsec = count;
        System.out.println("Number of matching results: " + count); // Hiển thị số kết quả
        return results;
    }

    private boolean matchesCriteria(ModsecLog modsecLog, String pat, String begin, String end, String beginTime, String endTime) {
        if (pat != null && !pat.isEmpty()) {
            // Kiểm tra từ khóa có xuất hiện trong một số trường của logEntry không
            if (!(modsecLog.getRequestUri().contains(pat) || modsecLog.getUserAgent().contains(pat) || modsecLog.getMessage().contains(pat) || modsecLog.getId().contains(pat) || modsecLog.getClientIp().contains(pat) || modsecLog.getAction().contains(pat) || modsecLog.getStatus().contains(pat))) {
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
            LocalDate logDate = LocalDate.parse(modsecLog.getDate(), outputFormatter);
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
            if (modsecLog.getTime().compareTo(beginTime) < 0) {
                return false;
            }
        }

        if (endTime != null && !endTime.isEmpty()) {
            if (modsecLog.getTime().compareTo(endTime) > 0) {
                return false;
            }
        }

        return true;
    }


    private ModsecLog parseLogEntry(String logBlock) {
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
        return new ModsecLog(id, date, time, clientIp, status, requestUri, userAgent, message, action);
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

    public HashMap<String, Integer> getIpMap() {
        return ipMap;
    }


    public HashMap<String, Integer> getTimeMap() {
        return timeMap;
    }

    public int [] getStatusMap() {
        return statusMap;
    }
    public int getTotal() {
        return total;
    }
    public int getTotalRequestMocsec() {
        return totalRequestMocsec;
    }
    public int getTotalFail() {
        return totalFail;
    }
    public int getTotalFailMoc() {
        return totalFailMoc;
    }

}
