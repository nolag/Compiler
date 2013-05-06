package java.lang;

public class Object{
    public Object() { }
    
    public static int x = 1;
    
    public static int getValue(){
    	return x; 
    }
    
    public static int test(){
        return getValue();
    }
}