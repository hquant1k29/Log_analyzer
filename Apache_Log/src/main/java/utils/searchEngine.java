package utils;
import log.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchEngine implements Comparator<AccessLog> {
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
        String fileName = "src/main/resources/data/apache_logs.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            stringCompare s = new stringCompare();
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\[([^]]*)]|\\S+"); // regrex split string to groups

            while ((line = br.readLine()) != null) {
                 total ++;
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
                    if(CheckInRange(date,beginDate,endDate) && CheckInRange(time.substring(0,5),beginTime,endTime)){
                        AccessLog a = new AccessLog(temp.get(0), temp.get(1), temp.get(2), date,time, temp.get(4), temp5, temp6, temp.get(7), temp.get(8));
                        list.add(a);
                        String[] httpPro = temp.get(4).split("\\s+");
                        try {
                            timeMap.merge(date,1,Integer::sum);
                            timeMap.merge(date + time.substring(0,2),1,Integer::sum);
                            timeMap.merge(date + time.substring(0,5),1,Integer::sum);
                            statusMap[temp5] ++;
                            ipMap.merge(temp.getFirst(),1,Integer::sum);
                            proto.merge(httpPro[0],1,Integer::sum);
                        }catch (Exception e) {
                            System.err.println("Error in here: " + e.getMessage());
                        }
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
    public boolean CheckInRange(String time,String begin,String end){
        if (!Objects.equals(begin, "") && !Objects.equals(end, "")) {
            return time.compareTo(begin) >= 0 && time.compareTo(end) <= 0;
        }
        else if (Objects.equals(begin, "") && !Objects.equals(end, "")){
            return time.compareTo(end) <= 0;
        }
        else if (!Objects.equals(begin, "") && Objects.equals(end, "")){
            return time.compareTo(begin) >= 0;
        }
        else if (Objects.equals(begin, "") && Objects.equals(end, "")){
            return true;
        }
        return false;
    }



    public ArrayList<AccessLog> SearchByDate(ArrayList<AccessLog> list,String begin,String end){
        ArrayList<AccessLog> temp = new ArrayList<>();
        if (!Objects.equals(begin, "") && !Objects.equals(end, "")) {
            for(AccessLog s : list) {
                if (s.getDate().compareTo(begin) >= 0 && s.getDate().compareTo(end) <= 0) {
                    temp.add(s);
                }

            }
        } else if (Objects.equals(begin, "") && !Objects.equals(end, "")) {
            for(AccessLog s : list) {
                if (s.getDate().compareTo(end) <= 0) {
                    temp.add(s);
                }
            }
        } else if (!Objects.equals(begin, "") && Objects.equals(end, "")) {
            for(AccessLog s : list) {
                if (s.getDate().compareTo(begin) >= 0) {
                    temp.add(s);
                }
            }
        } else if (Objects.equals(begin, "") && Objects.equals(end, "")) {
            temp.addAll(list);
        }
        return temp;
    }

    public HashMap<String, Integer> getIpMap() {
        return ipMap;
    }

    public HashMap<String, Integer> getProto() {
        return proto;
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

    public int getTotalFail() {
        return totalFail;
    }

    public double getTotalSize() {
        return totalSize;
    }


}
