public class WhileNestedIfElse {
    public WhileNestedIfElse() {}
    public int m() {
        int a = 0;
        while(a == 1) if (true) return 0;
        while(a == 2) if (true) return 0; else return 1;

        while(true){
            return 0;
        }
    }
}
