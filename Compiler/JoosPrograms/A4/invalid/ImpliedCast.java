package java.lang;

public class ImpliedCast{
    public ImpliedCast() { }
    public static int test(){
        String st = new String();
        Object o = st;
        o = new String();
        st = o;
        return 0;
    }
}