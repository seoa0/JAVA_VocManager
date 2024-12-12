package week15.lab01;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class MainFrame extends JFrame {
    VocManager vocManager;

    Container frame = getContentPane();
    JPanel northPanel = new JPanel();

    boolean flag = true; // 오름차순

    JTable table;
    DefaultTableModel model;

    String[] header = {"영단어", "뜻"};

    public MainFrame(String title, String filename) {
        super(title);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        initLayout();
        this.setVisible(true);
        initVocManager(filename);
    }

    private void initLayout() {
        initNorthPanel();
        initTable();
    }

    private void initVocManager(String filename) {
        this.vocManager = new VocManager("202311303 서아영"); // vocManager 초기화
        String str = this.vocManager.run(filename); //
        JOptionPane.showMessageDialog(this, str); // 단어장 생성 팝업 출력
        initTableData();
    }

    private void initTableData() {
        if(vocManager != null) {
            if(!vocManager.voc.isEmpty()) {
                List<Word> list = null; // awt x util o
                if(flag){
                    list = vocManager.voc.stream()
                            .sorted((o1, o2) -> o1.eng.compareTo(o2.eng))
                            .toList();
                } else {
                    list = vocManager.voc.stream()
                            .sorted((o1, o2) -> o1.eng.compareTo(o2.eng)*-1)
                            .toList();
                }
                for(Word word : list) {
                    model.addRow(new String[]{word.eng, word.kor});
                }
            }
        }
    }

    private void initTable() {
        this.model = new DefaultTableModel(header, 0); // 모델 초기화
        this.table = new JTable(model); // 테이블 초기화
        frame.add(new JScrollPane(table), BorderLayout.CENTER); // 프레임에 테이블 추가(스크롤, 센터)
    }

    private void initNorthPanel() {
        this.northPanel = new JPanel(); // 패널 초기화
        this.northPanel.add(new JLabel("검색할 단어")); // 패널에 라벨 추가
        JTextField text = new JTextField(10); // 텍스트필드 초기화
        text.addActionListener(e->{ // 엔터 입력 시
            if(vocManager != null) {
                Word w = vocManager.searchWord(text.getText()); // 입력한 값 넘겨주고 단어 받아옴
                if(w != null) {
                    removeTableData();
                    model.addRow(new String[]{w.eng, w.kor});
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "단어를 찾을 수 없습니다."); // outer class 객체 참조
                }
            }
            text.setText(""); // 입력 필드 비우기
        });
        this.northPanel.add(text); // 패널에 텍스트필드 추가

        JButton btn = new JButton("검색"); // 검색 버튼 초기화
        btn.addActionListener(e->{
            if(vocManager != null) {
                List<Word> list = vocManager.searchWord2(text.getText());
                if(!list.isEmpty()) {
                    removeTableData();
                    for(Word word : list) {
                        model.addRow(new String[]{word.eng, word.kor});
                    }
                }else{
                    JOptionPane.showMessageDialog(MainFrame.this, "단어를 찾을 수 없습니다.");
                }
            }
            text.setText("");
        });
        this.northPanel.add(btn); // 패널에 버튼 추가

        JRadioButton asc = new JRadioButton("ASC"); // 오름차순 라디오버튼 초기화
        asc.setSelected(true); // 디폴트 설정
        asc.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) { // 선택되어 있으면
                    flag = true;
                    removeTableData();
                    initTableData();
                }
            }
        });
        JRadioButton desc = new JRadioButton("DESC"); // 내림차순 라디오버튼 초기화
        desc.addItemListener(e-> {
            if(e.getStateChange() == ItemEvent.SELECTED) { // 선택되어 있으면
                flag = false;
                removeTableData();
                initTableData();
            }
        });

        ButtonGroup group = new ButtonGroup(); // 버튼 그룹 초기화
        group.add(asc); // 그룹에 버튼 추가
        group.add(desc); // 그룹에 버튼 추가
        this.northPanel.add(asc);
        this.northPanel.add(desc);

        frame.add(northPanel, BorderLayout.NORTH);
    }

    private void removeTableData() {
        if(model!=null && model.getRowCount() > 0) { // 표에 데이터가 하나라도 있으면
            model = new DefaultTableModel(header, 0); // 빈 데이터 모델 생성
            this.table.setModel(model); // 모델 바꿔치기
        }
    }

    public static void main(String[] args) {
        new MainFrame("202311303 서아영", "engdata/words.txt");
    }
}
