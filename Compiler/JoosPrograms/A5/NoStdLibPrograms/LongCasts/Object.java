package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l = (long)-1;
        int x = (int) l;
        if(l != -1) i = i + 1;
        if(x != -1) i = i + 2;
        l = -2147483649L;
        x = (int) l;
        if(x != 2147483647) i = i + 4;
        l = 0;
        l = x;
        if(l != 2147483647) i = i + 8;
        l = 2147483648L;
        x = (int) l;
        if(x != -2147483648) i = i + 16;
        l = 0;
        l = x;
        if(l != -2147483648) i = i + 32;
        l = 0;
        l = (long) x;
        if(l != -2147483648) i = i + 64;
        l = 117264517261579267L;
        x = (int)l;
        if (x != 136467459) i = i + 128;
        return i;
    }
}