package java.lang;

public class Super extends Base{
    public int x = 1;
    public Super(){ }
    
    public int helper(){
        return super.x;
    }
    
    public static int test(){
        Super s = new Super();
        return s.helper();
    }
}