package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l =  2147483647 << 2;
        long l2 = ((int)l) >> 2;
        if(l2 != -1) i = 1;
        l =  2147483648L;
        l2 = ((int)l) >> 2;
        if(l2 != -536870912) i = i + 2;
        l2 = ((int)l) >>> 2;
        if(l2 != 536870912) i = i + 4;
        return i;
    }
}