package week15;

public class Word {
    String eng;
    String kor;
    int count;

    public Word(String eng, String kor) {
        this.eng = eng;
        this.kor = kor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return eng.equals(word.eng);
    }

    @Override
    public String toString() {
        return eng+" : "+kor;
    }
}