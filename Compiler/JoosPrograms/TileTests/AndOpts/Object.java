package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        boolean b = true;
        b = true && b;
        b = !b && !b;
        b = false && b;
        b = !b & !b;
        return 0;
    }
}