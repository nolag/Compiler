public class ForNestedIfElse {
    public ForNestedIfElse() {}
    public int m() {
        int a = 0;
        for(; a == 1;) if (true) return 0;

        for(int i = 0; i < 10; i = i + 1){
            if (true) return 0; else return 1;
        }

        for(;;) if (true) return 0; else return 1;
    }
}
