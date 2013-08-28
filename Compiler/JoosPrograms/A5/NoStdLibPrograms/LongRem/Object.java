package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l1 =  1073741824L;
        long l2 = 2147483648L;
        if(l1 % l2 != 1073741824L) i = i + 1;
        l1 = -l1;
        if(l1 % l2 != -1073741824L) i = i + 2;
        l2 = -l2;
        if(l1 % l2 != -1073741824L) i = i + 4;
        l1 = 4294967296L;
        if(l1 % l2 != 0) i = i + 8;
        l1 = l1 + 1;
        if(l1 % l2 != 1) i = i + 16;
        return i;
    }
}