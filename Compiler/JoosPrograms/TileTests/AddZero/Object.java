package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 1;
        int j = 0 + i;
        j = i + 0;
        return j;
    }
}