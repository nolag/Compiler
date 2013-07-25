package java.lang;

public class Object{
    public Object() { }
    
    public static long sl = 429496729400L;
    
    public long l = 429496729400L;
    
    
    public static int test(){
        Object o = new Object();
        int i = 0;
        if(Object.l != 429496729400L) i = 1;
        if(o.l != 429496729400L) i = i + 2;
        o.l = 1;
        if(o.l != 1) i = i + 4;
        Object.sl = 1;
        if (Object.sl != 1) i = i + 8;
        
        o.l = 329496729400L;
        if(o.l != 329496729400L) i = i + 16;
        
        Object.sl = 329496729400L;
        if(Object.sl != 329496729400L) i = i + 32;
        
        return i;
    }
}