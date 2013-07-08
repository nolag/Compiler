package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        String st = new String();
        Object o = st;
        o = new String();
        char c = 10;
        short s = 10;
        byte b = 10;
        return 0;
    }
}