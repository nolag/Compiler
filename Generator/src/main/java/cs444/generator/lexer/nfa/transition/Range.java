package cs444.generator.lexer.nfa.transition;

public class Range {
    public final int from;
    public final int to;

    public Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean includes(int ch) {
        return from <= ch && ch <= to;
    }
}
