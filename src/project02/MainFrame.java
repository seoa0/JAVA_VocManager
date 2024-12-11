package project02;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * vocManager의 GUI를 제공하는 JFrame
 *
 *
 * @author 서아영
 * @since 2024-12-11
 */

public class MainFrame extends JFrame {
    VocManager vocManager;
    JTextArea displayArea;
    JTextField inputField;
    JButton inputButton;

    Container frame = getContentPane();
    JPanel menuPanel;
    JPanel northPanel;
    JPanel centerPanel;
    JPanel inputPanel;

    JTable table;
    DefaultTableModel model;

    private List<Word> voc10;
    private List<Word> option4;
    private int score = 0;
    private int currentQuestion = 0;
    private long startTime;
    private long endTime;

    String[] header = {"영단어", "뜻"};
    String[] header2 = {"영단어", "뜻", "틀린 횟수"};

    boolean flag = true; // 오름차순

    /**
     * MainFrame 생성자
     */
    public MainFrame(String title, String filename) {
        super(title);

        this.vocManager = new VocManager("서아영");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        initLayout();
        this.setVisible(true);
        initVocManager(filename);
    }
    /**
     * 단어장 불러오는 함수
     */
    private void initVocManager(String filename) {
        this.vocManager = new VocManager("202311303 서아영");
        String str = this.vocManager.run(filename);
        JOptionPane.showMessageDialog(this, str);
        initTable(); // 테이블 초기화
        initTableData();
    }
    /**
     * 레이아웃 설정하는 함수
     */
    private void initLayout() {
        this.setLayout(new BorderLayout());
        initMenuPanel();
        initCenterPanel();
        initNorthPanel();
        initInputPanel();
    }
    /**
     * 중간 패널 초기화하는 함수
     */
    private void initCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);
    }
    /**
     * 왼쪽 메뉴 설정하는 함수
     */
    private void initMenuPanel() {
        menuPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton menu1Button = new JButton("주관식 퀴즈");
        menu1Button.addActionListener(e -> handleMenu1());

        JButton menu2Button = new JButton("객관식 퀴즈");
        menu2Button.addActionListener(e -> handleMenu2());

        JButton menu3Button = new JButton("오답노트");
        menu3Button.addActionListener(e -> handleMenu3());

        JButton menu4Button = new JButton("단어 검색");
        menu4Button.addActionListener(e -> handleMenu4());

        JButton exitButton = new JButton("종료");
        exitButton.addActionListener(e -> System.exit(0));

        menuPanel.add(menu1Button);
        menuPanel.add(menu2Button);
        menuPanel.add(menu3Button);
        menuPanel.add(menu4Button);
        menuPanel.add(exitButton);

        frame.add(menuPanel, BorderLayout.WEST);
    }
    /**
     * menu4
     * 검색할 단어 입력받는 패널 초기화하는 함수
     */
    private void initNorthPanel() {
        this.northPanel = new JPanel();
        this.northPanel.add(new JLabel("검색할 단어"));
        JTextField text = new JTextField(10);

        text.addActionListener(e -> {
            if (vocManager != null) {
                Word w = vocManager.menu4(text.getText());
                if (w != null) {
                    removeTableData();
                    model.addRow(new String[]{w.eng, w.kor});
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "단어를 찾을 수 없습니다.");
                }
            }
            text.setText("");
        });
        this.northPanel.add(text);

        JButton btn = new JButton("검색");
        btn.addActionListener(e -> {
            if (vocManager != null) {
                Word w = vocManager.menu4(text.getText());
                if (w != null) {
                    removeTableData();
                    model.addRow(new String[]{w.eng, w.kor});
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "단어를 찾을 수 없습니다.");
                }
            }
            text.setText("");
        });
        this.northPanel.add(btn);

        JRadioButton asc = new JRadioButton("ASC");
        asc.setSelected(true);
        asc.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                flag = true;
                removeTableData();
                initTableData();
            }
        });
        JRadioButton desc = new JRadioButton("DESC");
        desc.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                flag = false;
                removeTableData();
                initTableData();
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(asc);
        group.add(desc);
        this.northPanel.add(asc);
        this.northPanel.add(desc);

        frame.add(northPanel, BorderLayout.NORTH);
    }
    /**
     * menu1,2
     * 답 입력받는 패널 초기화하는 함수
     */
    private void initInputPanel() {
        inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputButton = new JButton("입력");

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(inputButton, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);
    }
    /**
     * menu4
     */
    private void handleMenu4() {
        togglePanels(true, false);
        resetCenterPanel(); // 센터 패널 초기화
        initTable();        // 테이블 초기화
        initTableData();    // 테이블 데이터 추가
        frame.revalidate(); // 레이아웃 갱신
        frame.repaint();
    }
    /**
     * menu3
     */
    private void handleMenu3() {
        togglePanels(false, false);
        resetCenterPanel();
        initTable();
        initWrongTableData();
        frame.revalidate();
        frame.repaint();
    }
    /**
     * menu2
     */
    private void handleMenu2() {
        togglePanels(false, true);
        resetCenterPanel();
        initDisplayArea();
        Collections.shuffle(vocManager.voc);
        voc10 = vocManager.voc.subList(0, Math.min(10, vocManager.voc.size()));
        currentQuestion = 0;
        score = 0;
        startTime = System.nanoTime();
        showNextQuestion2();
        frame.revalidate();
        frame.repaint();
    }
    /**
     * menu1
     */
    private void handleMenu1() {
        togglePanels(false, true);
        resetCenterPanel();
        initDisplayArea();
        Collections.shuffle(vocManager.voc);
        voc10 = vocManager.voc.subList(0, Math.min(10, vocManager.voc.size()));
        currentQuestion = 0;
        score = 0;
        startTime = System.nanoTime();
        showNextQuestion1();
        frame.revalidate();
        frame.repaint();
    }
    /**
     * menu1,2,3,4
     * 중간 패널 초기화하는 함수
     */
    private void resetCenterPanel() {
        if (centerPanel != null) {
            frame.remove(centerPanel);
        }
        centerPanel = new JPanel(new BorderLayout());
        frame.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * menu1,2
     * 퀴즈 출력하는 패널 초기화하는 함수
     */
    private void initDisplayArea() {
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
    }
    /**
     * menu3,4
     * 테이블 초기화하는 함수
     */
    private void initTable() {
        model = new DefaultTableModel(header, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
    }
    /**
     * menu2
     * 객관식 퀴즈 생성하는 함수
     */
    private void showNextQuestion2() {
        if (currentQuestion >= voc10.size()) {
            endQuiz();
            return;
        }
        Word w = voc10.get(currentQuestion);
        option4 = vocManager.voc.stream()
                .filter(word -> !word.kor.equals(w.kor))
                .limit(3)
                .collect(Collectors.toList());
        option4.add(w);
        Collections.shuffle(option4);

        displayArea.setText("----- 객관식 퀴즈 " + (++currentQuestion) + "번 -----\n");
        displayArea.append("\"" + w.eng + "\"의 뜻은 무엇일까요?\n");

        for (int i = 0; i < option4.size(); i++) {
            displayArea.append((i + 1) + ") " + option4.get(i).kor + "\n");
        }
        inputField.setText("");
        // 전에 있던 리스너 제거
        for (ActionListener al : inputButton.getActionListeners()) {
            inputButton.removeActionListener(al);
        }
        inputButton.addActionListener(e -> {
            try {
                int answer = Integer.parseInt(inputField.getText().trim());
                if (answer < 1 || answer > 4) {
                    displayArea.append("1에서 4 사이의 숫자를 입력하세요.\n");
                    return;
                }
                if (option4.get(answer - 1).kor.equals(w.kor)) {
                    score++;
                    displayArea.append("정답입니다!\n");
                } else {
                    w.wrongCount++;
                    displayArea.append("틀렸습니다. 정답은 " + (option4.indexOf(w) + 1) + "번입니다.\n");
                }
                showNextQuestion2();
            } catch (NumberFormatException ex) {
                displayArea.append("숫자를 입력하세요.\n");
            }
        });
        // inputField의 Enter 입력 처리
        for (ActionListener al : inputField.getActionListeners()) {
            inputField.removeActionListener(al);
        }
        inputField.addActionListener(e -> inputButton.doClick());
    }

    /**
     * menu1
     * 주관식 퀴즈 생성하는 함수
     */
    private void showNextQuestion1() {
        if (currentQuestion >= voc10.size()) {
            endQuiz();
            return;
        }
        Word w = voc10.get(currentQuestion);
        displayArea.setText("----- 주관식 퀴즈 " + (currentQuestion+1) + "번 -----\n");
        displayArea.append("\"" + w.kor + "\"의 뜻을 가진 영어 단어는 무엇일까요?\n");
        inputField.setText("");
        // 전에 있던 리스너 제거
        for (ActionListener al : inputButton.getActionListeners()) {
            inputButton.removeActionListener(al);
        }

        // inputButton 이벤트 처리
        inputButton.addActionListener(e -> {
            String answer = inputField.getText().trim();
            if (answer.equalsIgnoreCase(w.eng)) {
                score++;
                displayArea.append("정답입니다!\n");
            } else {
                w.wrongCount++;
                displayArea.append("틀렸습니다. 정답은 \"" + w.eng + "\"입니다.\n");
            }
            currentQuestion++;
            showNextQuestion1(); // 다음 문제로 이동
        });
        for (ActionListener al : inputField.getActionListeners()) {
            inputField.removeActionListener(al);
        }
        inputField.addActionListener(e -> inputButton.doClick());
    }
    /**
     * menu1,2
     * 퀴즈 종료하고 점수, 시간 출력하는 함수
     */
    private void endQuiz() {
        endTime = System.nanoTime();
        long timeTaken = (endTime - startTime) / 1_000_000_000;
        displayArea.setText("퀴즈 종료!\n");
        displayArea.append("점수: " + score + " / " + voc10.size() + "\n");
        displayArea.append("소요 시간: " + timeTaken + "초");
        inputField.setText("");
        inputButton.setEnabled(true); // 버튼 다시 활성화
        inputField.requestFocus();   // 포커스 이동
    }
    /**
     * menu3,4
     * 테이블 비워주는 함수
     */
    private void removeTableData() {
        if (model != null) {
            int rowCount = model.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }
        }
    }
    /**
     * menu4
     * 단어장 데이터 불러오는 함수
     */
    private void initTableData() {
        if (vocManager != null) {
            if (!vocManager.voc.isEmpty()) {
                List<Word> list = flag ?
                        vocManager.voc.stream().sorted((o1, o2) -> o1.eng.compareTo(o2.eng)).toList() :
                        vocManager.voc.stream().sorted((o1, o2) -> o2.eng.compareTo(o1.eng)).toList();
                for (Word word : list) {
                    model.addRow(new String[]{word.eng, word.kor});
                }
            }
        }
    }
    /**
     * menu3
     * 오답 노트 데이터 불러오는 함수
     */
    private void initWrongTableData() {
        if (vocManager != null) {
            if (!vocManager.voc.isEmpty()) {
                List<Word> list = vocManager.voc.stream()
                        .filter(word -> word.wrongCount >= 1)
                        .sorted((w1, w2) -> Integer.compare(w2.wrongCount, w1.wrongCount))
                        .toList();
                DefaultTableModel model = new DefaultTableModel(header2, 0);
                for (Word word : list) {
                    model.addRow(new Object[]{word.eng, word.kor, word.wrongCount});
                }
                table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                centerPanel.add(scrollPane, BorderLayout.CENTER);
            }
        }
    }
    /**
     * menu1,2,3,4
     * 패널 설정 함수
     */
    private void togglePanels(boolean showNorthPanel, boolean showInputPanel) {
        if (northPanel != null) {
            northPanel.setVisible(showNorthPanel);
        }
        if (inputPanel != null) {
            inputPanel.setVisible(showInputPanel);
        }
    }
}
