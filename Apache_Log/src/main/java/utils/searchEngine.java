package utils;
import log.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchEngine implements Comparator<AccessLog> {
    private int total = 0;
    private int totalFail = 0;
    private int totalSize = 0;
    private final String fileName = "src/main/resources/data/apache_logs.txt";
    public searchEngine(){

    }
    public ArrayList<AccessLog> Search(String pat){
        ArrayList<AccessLog> list = new ArrayList();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            stringCompare s = new stringCompare();
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\[([^\\]]*)\\]|\\S+"); // regrex split string to groups
            while ((line = br.readLine()) != null) {
                total ++;
                if(s.BoyerMoore(line,pat) == 1) {
                    // Split line to fields
                    ArrayList<String> temp = new ArrayList();
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


                        } catch (NumberFormatException e) {
                            temp5 = 0;
                            temp6 = 0;
                        }
                    }
                    catch(StringIndexOutOfBoundsException e){
                        System.err.println("Error reading file: " + e.getMessage());
                    }
                    AccessLog a = new AccessLog(temp.get(0), temp.get(1), temp.get(2), date,time, temp.get(4), temp5, temp6, temp.get(7), temp.get(8));
                    list.add(a);

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return list;
    }
    public int compare(AccessLog a, AccessLog b) {
        return a.getDate().compareTo(b.getDate());
    }
    public ArrayList<AccessLog> SearchByDate(ArrayList<AccessLog> list,String begin,String end){
        ArrayList<AccessLog> temp = new ArrayList();
        if (begin != "" && end != "") {
            for(AccessLog s : list) {
                if (s.getDate().compareTo(begin) >= 0 && s.getDate().compareTo(end) <= 0) {
                    temp.add(s);
                }

            }
        } else if (begin == "" && end != "") {
            for(AccessLog s : list) {
                if (s.getDate().compareTo(end) <= 0) {
                    temp.add(s);
                }
            }
        } else if (begin != "" && end == "") {
            for(AccessLog s : list) {
                if (s.getDate().compareTo(begin) >= 0) {
                    temp.add(s);
                }
            }
        } else if (begin == "" && end == "") {
            for(AccessLog s : list) {
                temp.add(s);
            }
        }
        return temp;
    }

}
