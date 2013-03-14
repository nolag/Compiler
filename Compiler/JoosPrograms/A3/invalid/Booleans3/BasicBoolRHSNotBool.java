public class BasicBoolRHSNotBool{
    public BasicBoolRHSNotBool(){ }
    
    public boolean doStuff(){
        boolean b1 = true;
        int b2 = true;
        return (b1 && b2) || (b1 & b2) | (b1 || b2);
    }
}