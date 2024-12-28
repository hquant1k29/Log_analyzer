package utils;
import java.lang.String;

import static java.lang.Math.max;

public class stringCompare {
    public stringCompare (){
    }
    public int BoyerMoore(String txt,String pat){
        int n = txt.length();
        int m = pat.length();
        int [] badchar = new int [300];
        for(int i = 0; i < 300; i++){
            badchar[i] = -1;
        }
        for(int i = 0; i < pat.length(); i++){
            //badchar[(int)pat[i]] = i;
            badchar[pat.charAt(i)] = i;
        }
       int s = 0;
        while (s <= (n - m)) {
            int j = m - 1;
            while (j >= 0 && pat.charAt(j) == txt.charAt(s + j))
                j--;
            if (j < 0) {
                return 1;
            }

            else
                s += max(1, j - badchar[txt.charAt(s + j)]);
        }
        return 0;
    }
}
