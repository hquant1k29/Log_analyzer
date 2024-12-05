package utils;

import log.AccessLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loadData {
    private int total = 0;
    private int totalFail = 0;
    private int tmpSize = 0;
    private double totalSize = 0;

    private HashMap<String,Integer> dateMap = new HashMap<String,Integer>();
    private HashMap<String,Integer> timeMap = new HashMap<String,Integer>();
    private HashMap<String,Integer> proto = new HashMap<String,Integer>();
    private final String fileName = "src/main/resources/data/apache_logs.txt";
    public loadData(){

    }
    public ArrayList <AccessLog> load() {

        ArrayList<AccessLog> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            stringCompare s = new stringCompare();
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\[([^\\]]*)\\]|\\S+"); // regrex split string to groups
            while ((line = br.readLine()) != null) {
                ArrayList<String> temp = new ArrayList();
                total++;
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
                // handle format date,time
                int temp5 = 0, temp6 = 0;
                String date = "", time = "";
                try {
                    try {
                        temp5 = Integer.parseInt(temp.get(5));
                        temp6 = Integer.parseInt(temp.get(6));
                        String dateTime = temp.get(3);
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");

                        // Định dạng output mong muốn
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        // Phân tích và định dạng lại thời gian
                        LocalDateTime unFormatDateTime = LocalDateTime.parse(dateTime, inputFormatter.withZone(ZoneOffset.UTC));
                        String formattedTime = unFormatDateTime.format(outputFormatter);
                        date = formattedTime.substring(0, 10);
                        time = formattedTime.substring(11, 19);

                        if(temp5 >= 400) totalFail++;
                        tmpSize += temp6;
                        if(tmpSize > 1000000){
                            totalSize += (double)(tmpSize * 1.0)/1000000;
                            tmpSize = 0;
                        }
                        //totalSize += temp6;
                        AccessLog a = new AccessLog(temp.get(0), temp.get(1), temp.get(2), date,time, temp.get(4), temp5, temp6, temp.get(7), temp.get(8));
                        list.add(a);
                        String[] httpPro = temp.get(4).split("\\s+");
                        dateMap.merge(date,1,Integer::sum);
                        timeMap.merge(date + time.substring(0,2),1,Integer::sum);
                        proto.merge(httpPro[2],1,Integer::sum);
                    } catch (NumberFormatException e) {
                        temp5 = 0;
                        temp6 = 0;
                    }
                }
                catch(StringIndexOutOfBoundsException e){
                    System.err.println("Error reading file: " + e.getMessage());
                }
            }

        } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        return list;
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(int totalFail) {
        this.totalFail = totalFail;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(double totalSize) {
        this.totalSize = totalSize;
    }
    public HashMap<String, Integer> getDateMap() {
        return dateMap;
    }

    public HashMap<String, Integer> getTimeMap() {
        return timeMap;
    }

    public HashMap<String, Integer> getProto() {
        return proto;
    }
}
