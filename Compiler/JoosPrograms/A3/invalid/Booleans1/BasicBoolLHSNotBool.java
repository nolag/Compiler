public class BasicBoolLHSNotBool{
    public BasicBoolLHSNotBool(){ }
    
    public boolean doStuff(){
        int b1 = true;
        int b2 = true;
        return (b1 && b2) || (b1 & b2) | (b1 || b2);
    }
}