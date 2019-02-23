package java.lang;

public class Super extends Base{
    public Super(){ }
    
    public static int test(){
        Super s = new Super();
        Base b = s;
        b = (Base) s;
        return 123;
    }
}