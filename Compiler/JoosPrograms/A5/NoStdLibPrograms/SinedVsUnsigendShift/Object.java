package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = -1;
        if(i >> 2 < i >>> 2) return 0;
        return 1;
    }
}