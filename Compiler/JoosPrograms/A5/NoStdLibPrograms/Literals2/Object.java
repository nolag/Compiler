package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        int j = 100;
        if (99 >= j) i = 1;
        if (100 < j) i = i + 2;
        if (100 != j) i = i + 4;
        if (100 ==j) i = 8 + i;
        i = i - 8;
        if (j + 1 != 101) i = i + 16;
        if (101 < j) i = i + 32;
        if (101 <= j) i = i + 64;
        return i;
    }
}