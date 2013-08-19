package java.lang;

public class Object{
    public Object() { }
    
    public static int test(){
        int i = 0;
        long l = 4000000000000000L;
        l = l >> 2;
        if(l != 1000000000000000L) i = i + 1;
        l = 4000000000000000L;
        l = l >>> 2;
        if(l != 1000000000000000L) i = i + 2;
        l = 4000000000000000L;
        l = l << 2;
        if(l != 16000000000000000L) i = i + 4;
        
        l = 4000000000000000L;
        l = l >> 33;
        if(l != 465661L) i = i + 8;
        l = 4000000000000000L;
        l = l >>> 33;
        if(l != 465661L) i = i + 16;
        l = 4000000000000000L;
        l = l << 33;
        if(l != 2752825272230215680L) i = i + 32;
        
        return i;
    }
}