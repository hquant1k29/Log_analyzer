package service;

public class StringCompare {
    public int BoyerMoore(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int[] last = new int[256];

        for (int i = 0; i < 256; i++) last[i] = -1;
        for (int i = 0; i < m; i++) last[pattern.charAt(i)] = i;

        int i = m - 1;
        int j = m - 1;

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                if (j == 0) return 1; // Match found
                i--;
                j--;
            } else {
                i += m - Math.min(j, 1 + last[text.charAt(i)]);
                j = m - 1;
            }
        }
        return 0; // No match
    }
}
