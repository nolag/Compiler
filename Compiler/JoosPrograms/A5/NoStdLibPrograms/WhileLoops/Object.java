package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 10;
        int k = 20;
        char c = (char)1;
        
        while(c < 10) c = (char) (c + 1);
        
        while(k < 10) k = 10000000 + k;
        
        boolean b = true;
        
        while(b & true){
            i = i + 1;
            if(i > 5) b = false;
        }
        
        while(!b | false){
            if(i % 2 == 0) b = !b;
            i = i + 1;
        }
        
        return i + k + c;
    }
}