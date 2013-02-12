public class ForNestedIfElse {
    public ForNestedIfElse() {}
    public int m() {
        for(;;) if (true) return 0;
        for(;;) if (true) return 0; else return 1;
    }
}
