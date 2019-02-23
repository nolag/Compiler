package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        int j = 100;
        if (j <= 99) i = 1;
        if (j < 100) i = i + 2;
        if (j != 100) i = i + 4;
        if (j == 99) i = i + 8;
        if (j >= 101) i = i + 16;
        if (j > 100) i = i + 32;
        return i;
    }
}