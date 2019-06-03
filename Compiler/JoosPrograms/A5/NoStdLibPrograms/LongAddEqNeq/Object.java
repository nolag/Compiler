package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l = 1;
        //false
        if(l != 1) i = 1;
        if(l == 2) i = i + 2;
        //true
        if(l != 2) i = i + 4;
        if(l == 1) i = i + 8;

        l =  234529034523158534L;
        //false
        if(l != 234529034523158534L) i = i + 16;
        if(l == -2147483647L) i = i + 32;
        //true
        if(l != 234529034523158538L) i = i + 64;
        if(l == 234529034523158534L) i = i + 128;
        return i;
    }
}