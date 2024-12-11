package project02;

/**
 * 패키지 실행 main class
 *
 * @author 서아영
 * @since 2024-12-11
 */

public class TestMain {
    public static void main(String[] args) {
//        VocManager vocManager = new VocManager("단어장 프로그램");
//        vocManager.run("engdata/quiz.txt");
        MainFrame frame = new MainFrame("단어장프로그램", "engdata/quiz.txt");
    }
}

