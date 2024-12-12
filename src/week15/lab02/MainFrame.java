package week15.lab02;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainFrame extends JFrame implements ActionListener , ItemListener {
        Container frame = getContentPane();
        String[] fontTypeArr = {"CookieRunOTF", "Pretendard", "조선굴림체", "조선가는고딕"};

        JComboBox<String> fontTypeCB = new JComboBox(fontTypeArr);
        String fontType = "조선가는고딕";

        Integer[] fontSizeArr = {10, 14, 16, 18, 20, 22, 24};
        JComboBox<Integer> fontSizeCB = new JComboBox(fontSizeArr);
        int fontSize = 20;

        JCheckBox boldCheckBox = new JCheckBox("Bold");
        boolean bold = false;
        JCheckBox italicCheckBox = new JCheckBox("Italic");
        boolean italic = false;
        JTextField textField = new JTextField(15);

        JPanel northPanel = new JPanel();
        JLabel label = new JLabel("202311303 서아영", SwingConstants.CENTER);
        Font font = new Font(fontType, Font.PLAIN, fontSize);

    public MainFrame(String title) {
        super(title);
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        initLayout();
        this.setVisible(true);
    }

    public void initListner() {
        this.fontTypeCB.addActionListener(this);
        this.fontSizeCB.addActionListener(this);
        this.boldCheckBox.addItemListener(this);
        this.italicCheckBox.addItemListener(this);
        this.textField.addActionListener(this);
    }

    private void initLayout() {
        initListner();
        initMenu();
        initToolBar();

        this.fontTypeCB.setSelectedIndex(3);
        this.fontSizeCB.setSelectedIndex(4);
        this.northPanel.add(fontTypeCB);
        this.northPanel.add(fontSizeCB);
        this.northPanel.add(boldCheckBox);
        this.northPanel.add(italicCheckBox);
        this.northPanel.add(textField);
        frame.add(northPanel, BorderLayout.NORTH);

        label.setFont(font);
        frame.add(label, BorderLayout.CENTER);
    }

    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        for (String s : fontTypeArr) {
            JButton button = new JButton(s);
            button.addActionListener(this);
            toolBar.add(button);
        }
        toolBar.addSeparator();
        JButton btn1 = new JButton("Small");
        btn1.setActionCommand("10");
        btn1.addActionListener(this);
        toolBar.add(btn1);
        JButton btn2 = new JButton("Large");
        btn2.setActionCommand("24");
        btn2.addActionListener(this);
        toolBar.add(btn2);
        frame.add(toolBar, BorderLayout.SOUTH);
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("글자체");
        for (String s : fontTypeArr) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(this);
            menu1.add(item);
        }
        menuBar.add(menu1);

        JMenu menu2 = new JMenu("글자크기");
        for (int i : fontSizeArr) {
            JMenuItem item = new JMenuItem(String.valueOf(i));
            item.addActionListener(this);
            menu2.add(item);
        }
//        menuBar.add(menu2);
        menu1.add(menu2);

        this.setJMenuBar(menuBar);
    }


    public static void main(String[] args) {
        new MainFrame("202311303 서아영");
        // macOS에서 사용 가능한 글꼴 이름 가져오기
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        System.out.println("사용 가능한 글꼴:");
        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }

    // 현재 클래스의 멤버이므로 주소값을 다 가지고 있기 때문에 가능함
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.textField) {
            label.setText(textField.getText());
            textField.setText("");
        } else if (e.getSource() == this.fontTypeCB) {
            int index = fontTypeCB.getSelectedIndex();
            fontType = fontTypeArr[index];
        } else if (e.getSource() == this.fontSizeCB) {
            fontSize = fontSizeArr[fontSizeCB.getSelectedIndex()];
        }

        switch (e.getActionCommand()) {
            case "CookieRunOTF" -> fontType = "CookieRunOTF";
            case "Pretendard" -> fontType = "Pretendard";
            case "조선굴림체" -> fontType = "조선굴림체";
            case "조선가는고딕" -> fontType = "조선가는고딕";
            case "10" -> fontSize = 10;
            case "24" -> fontSize = 24;
        }
        setLabelFont();
    }
    private void setLabelFont() {
        if(bold && italic) {
            font = new Font(fontType, Font.BOLD|Font.ITALIC, fontSize);
        } else if (bold) {
            font = new Font(fontType, Font.BOLD, fontSize);
        } else if (italic) {
            font = new Font(fontType, Font.ITALIC, fontSize);
        } else {
            font = new Font(fontType, Font.PLAIN, fontSize);
        }
        label.setFont(font);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == this.boldCheckBox) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                bold = true;
            } else
                bold = false;
        } else if (e.getSource() == this.italicCheckBox) {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                italic = true;
            } else
                italic = false;
        }
        setLabelFont();
    }
}
