package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        if(-4 % 5 != -4) i = i + 1;
        if(4 % -5 != 4) i = i + 2;
        if(-4 % -5 != -4) i = i + 4;
        return i;
    }
}