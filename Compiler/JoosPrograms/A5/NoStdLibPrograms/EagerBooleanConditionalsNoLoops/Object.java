package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 10;
        int k = 20;
        boolean b = true;
        
        
        if(false | b){
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
        
        if(true & !b){
            b = !b;
            i = k * i;
        }else if(true){
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
        
        if(false | b) i = i + 1;
        
        return z - k - i + s - 1825;
    }
}