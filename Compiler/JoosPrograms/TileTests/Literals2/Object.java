package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        int j = 100;
        if (99 >= j) i = 1;
        if (100 > j) i = i + 2;
        if (100 != j) i = i + 4;
        if (99 == j) i = i + 8;
        if (101 <= j) i = i + 16;
        if (100 < j) i = i + 32;
        return i;
    }
}