package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l1 = -533639640599494656L;
        long l2 = -533639640599494655L;
       
        if (l1 / l2 != 1) i = 1;
        if (l1 % l2 != -1) i = i + 2;
        if (l2 / l1 != 0) i = i + 4;
        if (l2 % l1 != -533639640599494655L) i = i + 8;
        
        l1 = -l1;
        if (l1 / l2 != -1) i = 16;
        if (l1 % l2 != 1) i = i + 32;
        
        l1 = -l1;
        l2 = -l2;
        if (l1 % l2 != -1) i = i + 64;
        
        return i;
    }
}