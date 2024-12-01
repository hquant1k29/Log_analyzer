package utils;
import log.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchEngine {
    public searchEngine(){

    }
    public ArrayList<AccessLog> Search(String pat){
        ArrayList<AccessLog> list = new ArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader("/home/quan/Log_analyzer/Apache_Log/src/main/resources/apache_logs.txt"))) {
            String line;
            stringCompare s = new stringCompare();
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\[([^\\]]*)\\]|\\S+"); // regrex split string to groups
            while ((line = br.readLine()) != null) {
                if(s.BoyerMoore(line,pat) == 1){
                    // Split line to fields
                    ArrayList<String> temp = new ArrayList();
                    Matcher matcher = pattern.matcher(line);        //
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
                    int temp5,temp6;
                    try {
                         temp5 = Integer.parseInt(temp.get(5));
                         temp6 = Integer.parseInt(temp.get(6));
                    }
                    catch (NumberFormatException e) {
                         temp5 = 0;
                         temp6 = 0;
                    }

                    AccessLog a = new AccessLog(temp.get(0),temp.get(1),temp.get(2),temp.get(3),temp.get(4),temp5,temp6,temp.get(7),temp.get(8));
                    list.add(a);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return list;
    }
}
