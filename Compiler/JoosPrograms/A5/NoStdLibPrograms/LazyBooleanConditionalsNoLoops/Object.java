package java.lang;

public class Object{
    public Object() { }
    
public static int sideEffect = 0;
    
    public static int sideEffect2 = 0;
    
    public static boolean s1()
    {
        sideEffect = sideEffect - 1;
        return false;
    }
    
    public static boolean s2()
    {
        sideEffect2 = sideEffect2 + 10;
        return true;
    }
    
    public static boolean s22()
    {
        sideEffect2 = sideEffect2 -11;
        return true;
    }
    
    
    public static int test(){
        int i = 10;
        int k = 20;
        boolean b = true;
        boolean t = true;
        boolean f = false;
        
        if(b || 10 / 0 > 10){
            i = i + 10;
            k = k + 30;
            b = false;
        }else{
            i = -1;
            k = -1;
        }
        
        if(b){
            k = -1;
            i = -1;
        }else if(k > i){
            int j = 10;
            i = i + k + j;
        }
        
        if(t && !b){
            b = !b;
            i = k * i;
        }else if(t){
            i = -1;
            k = -1;
        }else{
            k = -2;
            i = -2;
        }
        
        char c = (char)1;
        
        i = -i;
        
        short s = (short)1;
        
        i = i - s;
        int z = i + k;
        z = z / 2;
        k = k % 3;
        
        if(f || b) i = i + 1;
        
        if(f && 10 / 0 > 400) i = -1;
        
        if (t || s1()) { }
        
        k = k + sideEffect;
        
        if (s2() && s22())
        {
            if (!s2() && s22())
            {
                sideEffect2 = sideEffect2 + 100;
            }
            
            sideEffect2 = sideEffect2 + 1;
        }
        
        k = k + sideEffect2;
        
        return z - k - i + s - 1825;
    }
}