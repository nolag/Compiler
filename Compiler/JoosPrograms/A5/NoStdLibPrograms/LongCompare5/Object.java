package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l = -533639640599494655L;
        long l2 = -1;
       
        if(l > l2) i = i + 1;
        if(l >= l2) i = i + 2;
        if(l2 < l) i = i + 4;
        if(l2 <= l) i = i + 8;
        if(!(l2 > l)) i = i + 16;
        if(!(l2 >= l2)) i = i + 32;
        
        l2 = -1;
        if(l >= l2) i = i + 64;

        l2 = l;
        if (!(l2 >= l)) i = i + 128;
        
        return i;
    }
}