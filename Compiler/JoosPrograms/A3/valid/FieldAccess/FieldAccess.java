package java.lang;
public class FieldAccess {
    public FieldAccess two = new FieldAccess2();
    
    public FieldAccess() { }
    
    public int getMyVal(){
        return ((FieldAccess2) two).i;
    }
    
    public FieldAccess getTwo(){
        return two;
    }
    
    public int getIntVal(){
        return getTwo().getMyVal();
    }
}