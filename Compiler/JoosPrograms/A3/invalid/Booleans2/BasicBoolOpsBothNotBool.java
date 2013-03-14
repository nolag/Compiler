public class BasicBoolOpsBothNotBool{
    public BasicBoolOpsBothNotBool(){ }
    
    public boolean doStuff(){
        int b1 = true;
        boolean b2 = true;
        return (b1 && b2) || (b1 & b2) | (b1 || b2);
    }
}