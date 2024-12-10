package week15;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {
    VocManager vocManager;

    Container frame = getContentPane();
    JPanel northPanel = new JPanel();

    boolean flag = true; // 오름차순

    JTable table;
    DefaultTableModel tableModel;

    String[] header = {"영단어", "뜻"};

    public MainFrame(String title) {
        super(title);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        initLayout();
        this.setVisible(true);
    }

    private void initLayout() {
    }

    public static void main(String[] args) {
        new MainFrame("202311303 서아영");
    }
}
