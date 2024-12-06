package chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.Map;

/**
 * Lớp để vẽ biểu đồ số lượng đăng nhập.
 */
public class LoginChart {

    /**
     * Hiển thị biểu đồ số lượng đăng nhập.
     *
     * @param loginCounts Map chứa thời gian và số lượng đăng nhập.
     */
    public void displayLoginChart(Map<String, Integer> loginCounts) {
        // Tạo dataset cho biểu đồ
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : loginCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Logins", entry.getKey());
        }

        // Tạo biểu đồ dạng cột
        JFreeChart barChart = ChartFactory.createBarChart(
                "Login Activity",        // Tiêu đề biểu đồ
                "Time Period",           // Nhãn trục X
                "Number of Logins",      // Nhãn trục Y
                dataset                  // Dataset
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
