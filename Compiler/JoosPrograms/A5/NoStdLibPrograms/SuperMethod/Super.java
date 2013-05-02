package java.lang;

public class Super{
    public Super(){ }
    public int getValue(){
        return 11;
    }
    
    public int helper(){
        return super.getValue();
    }
    
    public static int test(){
        Super s = new Super();
        return s.helper();
    }
}