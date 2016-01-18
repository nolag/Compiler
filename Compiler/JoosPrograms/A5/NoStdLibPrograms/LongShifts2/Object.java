package java.lang;

public class Object{
    public Object() { }
    
    public static int test(){
        int i = 0;
        long l = 4000000000000000L;
        l = l >> 0;
        if(l != 4000000000000000L) i = i + 1;
        l = 4000000000000000L;
        l = l >>> 0;
        if(l != 4000000000000000L) i = i + 2;
        l = 4000000000000000L;
        l = l << 0;
        if(l != 4000000000000000L) i = i + 4;
        
        l = -4000000000000000L;
        l = l >> 32;
        if(l != -931323L) i = i + 8;
        l = -4000000000000000L;
        l = l >>> 32;
        if(l != 4294035973L) i = i + 16;
        l = -4000000000000000L;
        l = l << 32;
        if(l != 7846959400739667968L) i = i + 32;
        
        return i;
    }
}