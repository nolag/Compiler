public class BasicBoolOps{
    public BasicBoolOps(){ }
    
    public boolean doStuff(){
        boolean b1 = true;
        boolean b2 = true;
        int i = 10;
        return (b1 && b2) || (b1 & b2) | (!b1 || !b2);
    }
}