package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        int number = 100;
        number = number * 2;
        if (number != 200) i = 1;
        number = number / 2;
        if (number != 50) i = 2;
        number = number * 8;
        if (number != 400) i = 4;
        number = number / 16;
        if (number != 25) i = 8;
        number = number * 16;
        if (number != 400) i = 16;
        return 0;
    }
}