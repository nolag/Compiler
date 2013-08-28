package java.lang;

public class Object{
    public Object() { }
    
    public static int test(){
        int i = 0;
        long l = 100;
        l = l * -2147483647;
        if(l != -214748364700L) i = 1;
        l = l * -2;
        if(l != 429496729400L) i = i + 2;
        l = -l;
        if (l != -429496729400L) i = i + 4;
        return i;
    }
}