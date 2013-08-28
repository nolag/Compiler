package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l1 =  2147483648L;
        long l2 = -9223372036854775808L;
        if(l1 / l2 != 0) i = 1;
        l1 = -9223372036854775808L;
        if(l1 / l2 != 1) i = i + 2;
        l1 = -9223372036854775808L;
        l2 = -4611686018427387904L;
        if(l1 / l2 != 2) i = i + 4;
        l2 = 4611686018427387904L;
        if(l1 / l2 != -2) i = i + 8;
        return i;
    }
}