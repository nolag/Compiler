package java.lang;

public class Object{
    public Object() { }
    
    public static int test(){
        int i = 0;
        long l = -9;
        l = l >> 63;
        if(l != -1) i = i + 1;
        l = -9;
        l = l >>> 63;
        if(l != 1) i = i + 2;
        l = -9;
        l = l << 63;
        if(l != -9223372036854775808L) i = i + 4;
        
        l = -4000000000000000L;
        l = l >> 33;
        if(l != -465662) i = i + 8;
        l = -4000000000000000L;
        l = l >>> 33;
        if(l != 2147017986) i = i + 16;
        l = -4000000000000000L;
        l = l << 33;
        if(l != -2752825272230215680L) i = i + 32;
        
        return i;
    }
}