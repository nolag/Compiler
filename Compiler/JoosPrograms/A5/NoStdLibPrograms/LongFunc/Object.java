package java.lang;

public class Object{
    public Object() { }
    
    public static long getLong(){
        long l = -2147483649L;
        return l;
    }
    
    public static long doLong(int i, long l, int j){
        return i + l + j;
    }
    
    public static int test(){
        int i = 0;
        long l = getLong();
        if(l != -2147483649L) i = 1;
        l = 0;
        l = doLong(1, 2147483649L, -1);
        if(l != 2147483649L) i = i + 2;
        return i;
    }
}