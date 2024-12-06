// chuyển đổi chuỗi thời gian từ log sang đúng với định dạng Java
// So sánh thời gian để lọc log theo khoảng thời gian

package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TimeUtils {

    // Map chuyển đổi tháng từ tiếng Việt sang tiếng Anh
    private static final Map<String, String> monthMap = new HashMap<>();

    static {
        monthMap.put("Thg 1", "Jan");
        monthMap.put("Thg 2", "Feb");
        monthMap.put("Thg 3", "Mar");
        monthMap.put("Thg 4", "Apr");
        monthMap.put("Thg 5", "May");
        monthMap.put("Thg 6", "Jun");
        monthMap.put("Thg 7", "Jul");
        monthMap.put("Thg 8", "Aug");
        monthMap.put("Thg 9", "Sep");
        monthMap.put("Thg 10", "Oct");
        monthMap.put("Thg 11", "Nov");
        monthMap.put("Thg 12", "Dec");
    }

    /**
     * Chuyển đổi chuỗi thời gian từ log sang dạng chuẩn trong Java.
     * @param timestampStr Chuỗi thời gian trong log (vd: "Thg 11 21 23:50:03").
     * @return Đối tượng Date nếu thành công, null nếu thất bại.
     */

    public static Date parseTimestamp(String timestampStr) {
        try {
            // Tách chuỗi thành các phần
            String[] parts = timestampStr.split(" ");
            if (parts.length < 4) {
                throw new ParseException("Chuỗi thời gian không đầy đủ: " + timestampStr, 0);
            }

            String month = parts[0] + " " + parts[1]; // vd: "Thg 12"
            String day = parts[2];                   // vd: "04"
            String time = parts[3];                  // vd: "15:53:25"

            // Chuyển tháng từ tiếng Việt sang tiếng Anh
            String monthInEnglish = monthMap.getOrDefault(month, null);
            if (monthInEnglish == null) {
                throw new ParseException("Tháng không hợp lệ: " + month, 0);
            }

            // Thêm năm hiện tại cho đúng định dạng java
            String currentYear = String.valueOf(java.time.LocalDate.now().getYear());

            // Tạo chuỗi thời gian phù hợp với định dạng SimpleDateFormat
            String formattedTimestamp = currentYear + " " + monthInEnglish + " " + day + " " + time;

            // Sử dụng Locale.US để đảm bảo parse đúng các từ viết tắt tháng
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss", Locale.US);
            // Chú ý rằng phải thêm định dạng locale.us để java nhận dạng đúng là của nước mỹ
            // Nếu không dùng Locale, hệ thống tự dùng cuủa chính máy tính (Hiện là VN)

            return sdf.parse(formattedTimestamp);

        } catch (Exception e) {
            // In lỗi và trả về null
            System.err.println("Lỗi khi chuyển đổi thời gian từ log: " + timestampStr);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Kiểm tra xem thời gian trong log có nằm trong khoảng thời gian hay không.
     * @param logTime Thời gian trong log.
     * @param startTime Thời gian bắt đầu.
     * @param endTime Thời gian kết thúc.
     * @return true nếu logTime nằm trong khoảng thời gian, ngược lại false.
     */
    public static boolean isWithinTimeRange(Date logTime, Date startTime, Date endTime) {
        return logTime != null && logTime.after(startTime) && logTime.before(endTime);
    }
}

