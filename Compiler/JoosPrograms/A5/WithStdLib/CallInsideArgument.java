public class CallInsideArgument{
    public CallInsideArgument() { }

    public static int test() {
        System.out.print(CallInsideArgument.str(true));
        return 123;
    }

    public static String str(boolean b){
        return "a";
    }
}
