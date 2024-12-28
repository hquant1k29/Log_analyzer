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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loadData {
    private int total = 0;
    private int totalFail = 0;
    private int tmpSize = 0;
    private double totalSize = 0;

    private HashMap<String,Integer> dateMap = new HashMap<>();
    private Map<Map<String,String>,Integer> timeMap = new HashMap<>();
    private HashMap<String,Integer> proto = new HashMap<>();

    public loadData(){

    }
    public void loadDataMap() {

        String fileName = "src/main/resources/data/apache_logs.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\[([^]]*)]|\\S+"); // regrex split string to groups
            while ((line = br.readLine()) != null) {
                total++;
                ArrayList<String> temp = getStrings(pattern, line);
                // handle format date,time
                int temp5 , temp6;
                String date , time, dateTime;
                try {
                    try {
                        temp5 = Integer.parseInt(temp.get(5));
                        temp6 = Integer.parseInt(temp.get(6));
                        dateTime = temp.get(3);
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
                            totalSize += (tmpSize * 1.0)/1000000;
                            tmpSize = 0;
                        }
                        String[] httpPro = temp.get(4).split("\\s+");
                        //timeMap.merge(date,1,Integer::sum);
                        //timeMap.merge(date + time.substring(0,2),1,Integer::sum);
                        //System.out.println(httpPro[0]);
                        proto.merge(httpPro[0],1,Integer::sum);
                    } catch (NumberFormatException e) {
                        temp5 = 0;
                        temp6 = 0;
                        System.err.println("Error Number Format: " + e.getMessage());
                    }
                }
                catch(StringIndexOutOfBoundsException e){
                    System.err.println("Error reading file: " + e.getMessage());
                }
            }

        } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
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
    public int getTotal() {
        return total;
    }

    public int getTotalFail() {
        return totalFail;
    }


    public double getTotalSize() {
        return totalSize;
    }

    public HashMap<String, Integer> getDateMap() {
        return dateMap;
    }

//    public HashMap<String, Integer> getTimeMap() {
//        return timeMap;
//    }

    public HashMap<String, Integer> getProto() {
        return proto;
    }
}
