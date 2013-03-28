package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 10;
        int k = 20;
        char c = (char)1;
        
        for(int x = 1; x < 10; x = x + 1) c = (char) (c + 1);
        
        for(;k < 10; k = 10000000 + k);
        
        for(boolean b = true; b & true; b = i < 105) i = i + 1;
        
        for(boolean b = true; !b | false;  b = i % 2 != 0) i = i + 1;
        
        int z = 0;
        
        return i + k + c + z;
    }
}