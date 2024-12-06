package chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.Map;

/**
 * Lớp để vẽ biểu đồ số lượng đăng nhập theo giờ.
 */
public class LoginChart {

    /**
     * Hiển thị biểu đồ số lượng đăng nhập theo giờ.
     *
     * @param hourlyLogins Map chứa giờ và số lượng đăng nhập.
     * @param date         Ngày cần phân tích (để hiển thị trên tiêu đề biểu đồ).
     */
    public void displayLoginChartByHour(Map<Integer, Integer> hourlyLogins, String date) {
        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Integer> entry : hourlyLogins.entrySet()) {
            String hourLabel = entry.getKey() + ":00"; // Hiển thị giờ dưới dạng 0:00, 1:00, ...
            dataset.addValue(entry.getValue(), "Logins", hourLabel);
        }

        // Tạo biểu đồ dạng cột
        JFreeChart barChart = ChartFactory.createBarChart(
                "Login Activity by Hour (" + date + ")", // Tiêu đề biểu đồ
                "Hour",                                  // Nhãn trục X
                "Number of Logins",                      // Nhãn trục Y
                dataset                                  // Dataset
        );

        // Hiển thị biểu đồ
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        JFrame frame = new JFrame("Login Chart");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
