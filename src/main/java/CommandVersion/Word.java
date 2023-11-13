package CommandVersion;

public class Word implements Comparable<Word>{
    private String word_target;
    private String word_explain;

    String getWordTarget() {
        return word_target;
    }

    String getWordExplain() {
        return word_explain;
    }

    void setWordTarget(String word_target) {
        this.word_target = word_target;
    }

    void setWordExplain(String word_explain) {
        this.word_explain = word_explain;
    }

    Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    @Override
    public int compareTo(Word w){
        return this.word_target.compareTo(w.getWordTarget());
    }
}
