package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        if(2147483647 + 1L != 2147483648L) i = 1;
        if(2147483647L * 2 != 4294967294L) i = i + 2;
        if(4294967294L / 2L != 2147483647L) i = i + 4;
        if(2147483647L << 1 != 4294967294L) i = i + 8;
        if(2147483647 + 1 != -2147483648) i = 16;
        if(4294967294L >> 1 != 2147483647L) i = i + 32;
        if(4294967294L >>> 1 != 2147483647L) i = i + 64;
        return i;
    }
}
