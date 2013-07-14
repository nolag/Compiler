package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        int j =  32768;
        if((short) j != -32768) i = i + 1;
        short s = -1;
        if((int) s != -1) i = i + 2;
        return i;
    }
}