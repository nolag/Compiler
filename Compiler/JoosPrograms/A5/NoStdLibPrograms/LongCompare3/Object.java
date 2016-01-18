package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l = 5;
        long l2 = 6;
       
        if(l > l2) i = i + 1;
        if(l >= l2) i = i + 2;
        if(l2 < l) i = i + 4;
        if(l2 <= l) i = i + 8;
        if(!(l2 > l)) i = i + 16;
        if(!(l2 >= l2)) i = i + 32;
        
        return i;
    }
}