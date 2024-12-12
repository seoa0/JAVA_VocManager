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
 * 요구사항 2,3번(vocManager)을 제외한 모든 요구사항은 이 클래스에서 구현되었다.
 * <<< 체크리스트 >>>
 * 1. 사용자 입력 실수, 파일 오류 등을 고려한 예외처리를 수행하였는가? yes
 * 2. 텍스트 파일을 읽어서 Word 객체를 생성한 후, 컬렉션 클래스의 저장구조에 잘 저장하였는가? yes
 * 3. 파일에 저장된 패턴(영어와 단어가 나열된 형식)이 동일한 파일도 읽어서 처리할 수 있는가? yes
 * 4. 메뉴는 5번 종료 메뉴를 수행할 때까지 반복 수행되는가? yes
 * 5. 1번 주관식 퀴즈 메뉴 관련
 *      A. 퀴즈 10문제가 중복되지 않도록 처리하였는가? yes
 *      B. 정답을 정확하게 체크하는가? yes
 *      C. 뜻이 같은 단어에 대한 정답처리를 잘 하였는가? yes
 *      D. 10문제가 연속으로 출제되는가? yes
 *      E. 10문제의 퀴즈를 푼 시간을 측정하여 출력하였는가? yes
 *      F. 10문제 퀴즈 채점한 결과가 출력되었는가? yes
 *      G. 퀴즈 출제된 단어의 출제회수가 누적되는가? yes
 *      H. 오답시 오답회수가 누적되는가? yes
 *      I. 뜻이 같은 단어에 대한 출제회수 처리는 잘 하였는가? yes
 *      J. 뜻이 같은 단어에 대한 오답회수 처리를 잘 하였는가? yes
 * 6. 2번 객관식 퀴즈 메뉴 관련
 *      A. 퀴즈의 선지로 선택된 4개의 영어 단어에는 중복이 없는가? yes
 *      B. 선지를 구성하는 4개의 영어 단어의 뜻도 중복체크 하였는가? yes
 *      C. 정답의 위치는 랜덤하게 설정하였는가? yes
 *      D. 사용자가 답을 입력하면 정확하게 정답과 오답을 출력하는가? yes
 *      E. 10문제가 연속으로 출제되는가? yes
 *      F. 10문제의 퀴즈를 푼 시간을 측정하여 출력하였는가? yes
 *      G. 10문제 퀴즈 채점한 결과가 출력되었는가? yes
 *      H. 퀴즈 출제된 단어의 출제회수가 누적되는가? yes
 *      I. 퀴즈에서 틀린 단어에 대한 오답처리가 제대로 되는가? yes
 *      J. 오답 회수는 누적되는가? yes
 * 7. 3번 오답노트 메뉴 관련
 *      A. 퀴즈에서 틀린 문제는 오답노트에 잘 저장하였는가? yes
 *      B. 오답회수가 가장 높은 단어순으로 출력되는가? yes
 *      C. 틀린 단어가 없는 경우에 대한 처리도 하였는가? yes
 * 8. 4번 단어 검색 메뉴 관련
 *      A. 입력된 단어를 찾은 경우 단어의 뜻을 보여주는가? yes
 *      B. 단어장에 등록되지 않은 단어의 경우 처리를 하였는가? yes
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
    String[] header2 = {"영단어", "뜻", "틀린 횟수", "출력 횟수"};

    boolean flag = true;

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
        initTable();
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
        // 4. 메뉴는 5번 종료 메뉴를 수행할 때까지 반복 수행되는가?
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
                    // 8-A. 입력된 단어를 찾은 경우 단어의 뜻을 보여주는가?
                    model.addRow(new String[]{w.eng, w.kor});
                } else {
                    // 8-B. 단어장에 등록되지 않은 단어의 경우 처리를 하였는가?
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
        resetCenterPanel();
        initTable();
        initTableData();
        frame.revalidate();
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
        // 5-A. 퀴즈 10문제가 중복되지 않도록 처리하였는가?
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
        // 6-E. 10문제가 연속으로 출제되는가?
        if (currentQuestion >= voc10.size()) {
            // 6-H. 퀴즈 출제된 단어의 출제회수가 누적되는가?
            voc10.forEach(word -> word.showCount++);
            endQuiz();
            return;
        }
        Word w = voc10.get(currentQuestion);
        // 6-A. 퀴즈의 선지로 선택된 4개의 영어 단어에는 중복이 없는가?
        option4 = vocManager.voc.stream()
        // 6-B. 선지를 구성하는 4개의 영어 단어의 뜻도 중복체크 하였는가?
                .filter(word -> !word.kor.equals(w.kor))
                .limit(3)
                .collect(Collectors.toList());
        option4.add(w);
        // 6-C. 정답의 위치는 랜덤하게 설정하였는가?
        Collections.shuffle(option4);

        displayArea.setText("----- 객관식 퀴즈 " + (currentQuestion+1) + "번 -----\n");
        displayArea.append("\"" + w.eng + "\"의 뜻은 무엇일까요?\n");

        for (int i = 0; i < option4.size(); i++) {
            displayArea.append((i + 1) + ") " + option4.get(i).kor + "\n");
        }
        inputField.setText("");
        for (ActionListener al : inputButton.getActionListeners()) {
            inputButton.removeActionListener(al);
        }
        inputButton.addActionListener(e -> {
            try {
                int answer = Integer.parseInt(inputField.getText().trim());
                inputField.setEnabled(false);
                inputButton.setEnabled(false);
                if (answer < 1 || answer > 4) {
                    displayArea.append("1에서 4 사이의 숫자를 입력하세요.\n");
                    inputField.setEnabled(true);
                    inputButton.setEnabled(true);
                    inputField.requestFocusInWindow();
                    return;
                }
                // 6-D. 사용자가 답을 입력하면 정확하게 정답과 오답을 출력하는가?
                if (option4.get(answer - 1).kor.equals(w.kor)) {
                    score++;
                    displayArea.append("정답입니다!\n");
                } else {
                    // 6-I. 퀴즈에서 틀린 단어에 대한 오답처리가 제대로 되는가?
                    // 6-J. 오답 회수는 누적되는가?
                    w.wrongCount++;
                    displayArea.append("틀렸습니다. 정답은 " + (option4.indexOf(w) + 1) + "번입니다.\n");
                }
                Timer timer = new Timer(2000, event -> {
                    inputField.setEnabled(true);
                    inputButton.setEnabled(true);
                    inputField.requestFocusInWindow();
                    currentQuestion++;
                    showNextQuestion2();
                });
                timer.setRepeats(false);
                timer.start();
            } catch (NumberFormatException ex) {
                displayArea.append("숫자를 입력하세요.\n");
                inputField.setEnabled(true);
                inputButton.setEnabled(true);
                inputField.requestFocusInWindow();
            }
        });
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
        // 5-D. 10문제가 연속으로 출제되는가?
        if (currentQuestion >= voc10.size()) {
            // 5-G. 퀴즈 출제된 단어의 출제회수가 누적되는가?
            // 5-I. 뜻이 같은 단어에 대한 출제회수 처리는 잘 하였는가?
            for (Word word : voc10) {
                for (Word v : vocManager.voc) {
                    if (v.kor.equals(word.kor)) {
                        v.showCount++;
                    }
                }
            }
            endQuiz();
            return;
        }
        Word w = voc10.get(currentQuestion);
        displayArea.setText("----- 주관식 퀴즈 " + (currentQuestion + 1) + "번 -----\n");
        displayArea.append("\"" + w.kor + "\"의 뜻을 가진 영어 단어는 무엇일까요?\n");
        inputField.setText("");
        for (ActionListener al : inputButton.getActionListeners()) {
            inputButton.removeActionListener(al);
        }
        inputButton.addActionListener(e -> {
            String answer = inputField.getText().trim();
            inputField.setEnabled(false);
            inputButton.setEnabled(false);
            // 5-B. 정답을 정확하게 체크하는가?
            // 5-C. 뜻이 같은 단어에 대한 정답처리를 잘 하였는가?
            boolean isCorrect = false;
            for (Word word : vocManager.voc) {
                if (word.kor.equals(w.kor) && word.eng.equalsIgnoreCase(answer)) {
                    isCorrect = true;
                    break;
                }
            }
            if (isCorrect) {
                score++;
                displayArea.append("정답입니다!\n");
            } else {
                // 5-H. 오답시 오답회수가 누적되는가?
                // 5-J. 뜻이 같은 단어에 대한 오답회수 처리를 잘 하였는가?
                for (Word word : vocManager.voc) {
                    if (word.kor.equals(w.kor)) {
                        word.wrongCount++;
                    }
                }
                displayArea.append("틀렸습니다. 정답은 \"" + w.eng + "\"입니다.\n");
            }
            Timer timer = new Timer(2000, event -> {
                inputField.setEnabled(true);
                inputButton.setEnabled(true);
                inputField.requestFocusInWindow();
                currentQuestion++;
                showNextQuestion1();
            });
            timer.setRepeats(false);
            timer.start();
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
        // 5-E. 10문제의 퀴즈를 푼 시간을 측정하여 출력하였는가?
        // 6-F. 10문제의 퀴즈를 푼 시간을 측정하여 출력하였는가?
        long timeTaken = (endTime - startTime) / 1_000_000_000;
        timeTaken -= 20;
        displayArea.setText("퀴즈 종료!\n");
        // 5-F. 10문제 퀴즈 채점한 결과가 출력되었는가?
        // 6-G. 10문제 퀴즈 채점한 결과가 출력되었는가?
        displayArea.append("점수: " + score + " / " + voc10.size() + "\n");
        displayArea.append("소요 시간: " + timeTaken + "초");
        inputField.setText("");
        inputButton.setEnabled(true);
        inputField.requestFocus();
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
    // 7-A. 퀴즈에서 틀린 문제는 오답노트에 잘 저장하였는가?
    private void initWrongTableData() {
        if (vocManager != null) {
            if (!vocManager.voc.isEmpty()) {
                List<Word> list = vocManager.voc.stream()
                        .filter(word -> word.wrongCount >= 1)
                        // 7-B. 오답회수가 가장 높은 단어순으로 출력되는가?
                        .sorted((w1, w2) -> Integer.compare(w2.wrongCount, w1.wrongCount))
                        .toList();

                if (list.isEmpty()) {
                    // 7-C.틀린 단어가 없는 경우에 대한 처리도 하였는가?
                    displayArea.setText("틀린 문제가 없습니다.\n");
                } else {
                    DefaultTableModel model = new DefaultTableModel(header2, 0);
                    for (Word word : list) {
                        model.addRow(new Object[]{word.eng, word.kor, word.wrongCount, word.showCount});
                    }
                    table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);
                    centerPanel.add(scrollPane, BorderLayout.CENTER);
                }
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
