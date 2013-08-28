package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l1 =  2147483648L;
        long l2 = 1073741824L;
        if(l1 / l2 != 2) i = i + 1;
        l1 = -l1;
        if(l1 / l2 != -2) i = i + 2;
        l1 = -l1 + 1;
        if(l1 / l2 != 2) i = i + 4;
        l2 = -l2;
        if(l1 / l2 != -2) i = i + 8;
        l1 = -l1;
        if(l1 / l2 != 2) i = i + 16;
        l1 = 100;
        l2 = 10;
        if(l1 / l2 != 10) i = i + 64;
        return i;
    }
}