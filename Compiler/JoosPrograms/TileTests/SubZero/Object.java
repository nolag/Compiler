package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = -1;
        int j = i - 0;
        i = 0 - j;
        return i;
    }
}