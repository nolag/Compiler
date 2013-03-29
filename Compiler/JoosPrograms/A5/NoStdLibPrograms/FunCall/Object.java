package java.lang;

public class Object{
    public Object() { }
    public static int stuff(int i, int j, boolean b){
        if(b){
            return 100;
        }
        
        int x = 2;
        
        return i * j - x + 2;
    }
    
    public static int test(){
        int i = Object.stuff(1, -1, false);
        int j = Object.stuff(1, 1, true);
        int x = 100;
        return i + j - x + 1;
    }
}