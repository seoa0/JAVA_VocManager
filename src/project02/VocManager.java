package project02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class VocManager {
    String name;
    List<Word> voc = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public VocManager(String name) {
        this.name = name;
    }

    public void addWord(Word w) {
        voc.add(w);
    }

    public String run(String filename) {
        try {
            Scanner scan = new Scanner(new File(filename));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                StringTokenizer st = new StringTokenizer(line, "\t");
                String eng = st.nextToken();
                String kor = st.nextToken();
                this.addWord(new Word(eng.trim(), kor.trim()));
            }
            return name + "의 단어장이 생성되었습니다.";
//            menu();
        } catch (FileNotFoundException e) {
            return "파일을 찾을 수 없습니다.";
        }
    }


    public Word menu4(String eng) {
        // 단어검색
        int index = voc.indexOf(new Word(eng,""));
        if(index != -1){
            voc.get(index).count++;
            System.out.println(voc.get(index));
            return voc.get(index);
        }else{
            System.out.println("단어장에 등록된 단어가 아닙니다.");
            return null;
        }
    }

    public void menu3() {
        // 오답노트
        List<Word> wrongNotes = voc.stream()
                .filter(w -> w.wrongCount >= 1)
                .sorted(Comparator.comparingInt((Word w) -> w.wrongCount).reversed()) // 내림차순 정렬
                .collect(Collectors.toList()); // 일반적인 toList()랑 다르게 가변리스트로 반환

        if (wrongNotes.isEmpty()) {
            System.out.println("틀린 문제가 없습니다.");
        } else {
            wrongNotes.forEach(System.out::println); // 정렬된 결과 출력
        }
    }


    public void menu2() {
        // 객관식 퀴즈
        Collections.shuffle(voc);
        List<Word> voc10 = voc.subList(0,10);
        List<Word> option4 = new ArrayList<>();

        int score = 0; // 맞춘 점수
        int i = 1; // 문제 번호
        // 시작시간 측정
        long startTime = System.nanoTime();
        for(Word w : voc10) {
            w.showCount++; // 출제 수 증가
            System.out.println("----- 객관식 퀴즈 " + i++ + "번 -----");
            System.out.println("\"" + w.eng + "\"의 뜻은 무엇일까요?");

            option4 = voc.stream()
                    .filter(a -> !a.kor.equals(w.kor)) // 동일한 뜻 거르기
                    .limit(3) // 오답 선지 3개
                    .collect(Collectors.toList()); // stream 객체를 list 객체로 변환
            option4.add(w); // 정답 선지 추가
            Collections.shuffle(option4); // 선지 섞기

            int j = 0;
            for(var o : option4) {
                System.out.println(++j + ") " + o.kor);

            }
            int answer;
            while (true) {
                System.out.print("답을 입력하세요: ");
                try {
                    answer = scanner.nextInt();
                    if (answer < 1 || answer > 4) {
                        System.out.println("1에서 4 사이의 숫자를 입력하세요.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("숫자를 입력하세요.");
                }
            }
            boolean isCorrect = w.kor.equals(option4.get(answer - 1).kor);

            if(isCorrect) {
                score++; // 점수 증가
                System.out.println("정답입니다.");
            }
            else {
                w.wrongCount++; // 오답 개수 증가
                int temp = option4.indexOf(w) + 1; // 정답 위치 찾기
                System.out.println("틀렸습니다. 정답은 " + temp + "번입니다.");
            }
            option4.clear();
        }
        // 끝 시간 측정
        long endTime = System.nanoTime();
        long time = (endTime - startTime) / 1_000_000_000; // 초 단위로 변환
        System.out.println(name + "님, 10문제 중 " + score + "개 맞추셨고, 총 "+time+"초 소요되었습니다.");
    }

    public void menu1() {
        // 주관식 퀴즈
        Collections.shuffle(voc);
        List<Word> voc10 = voc.subList(0,10);
        int score = 0; // 맞춘 점수
        int i = 1; // 문제 번호
        // 시작시간 측정
        long startTime = System.nanoTime();

        for(Word w : voc10) {
            voc.stream()
                    .filter(word -> word.kor.equals(w.kor)) // 정답과 뜻이 동일한 단어만 필터링
                    .forEach(word -> word.showCount++); // 오답 개수 증가
            System.out.println("----- 주관식 퀴즈 " + i++ + "번 -----");
            System.out.println("\"" + w.kor + "\"의 뜻을 가진 영어 단어는 무엇일까요?");
            System.out.print("답을 입력하세요: ");
            String answer = scanner.nextLine();

            boolean isCorrect = voc.stream().anyMatch(word -> word.eng.equals(answer)); // 여러 단어 중 하나 일치 시 true

            if(isCorrect) {
                score++; // 점수 증가
                System.out.println("정답입니다.");
            }
            else {
                voc.stream()
                        .filter(word -> word.kor.equals(w.kor)) // 정답과 뜻이 동일한 단어만 필터링
                        .forEach(word -> word.wrongCount++); // 오답 개수 증가
                System.out.println("틀렸습니다. 정답은 " + w.eng + "입니다.");
            }
        }
        // 끝 시간 측정
        long endTime = System.nanoTime();
        long time = (endTime - startTime) / 1_000_000_000; // 초 단위로 변환
        System.out.println(name + "님, 10문제 중 " + score + "개 맞추셨고, 총 "+time+"초 소요되었습니다.");
    }

}

