package project02;

import java.util.Objects;

public class Word {
    String eng;
    String kor;
    int showCount=0;
    int wrongCount=0;
    int count=0;

    public Word(String eng, String kor) {
        this.eng = eng;
        this.kor = kor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(eng, word.eng); //&& Objects.equals(kor, word.kor);
    }

    @Override
    public String toString() {
        String str = eng+" : "+kor;
        str += "\n출제횟수 : " + showCount;
        str += "    오답횟수 : " + wrongCount + "\n-------------------------";
        return str;
    }
}
