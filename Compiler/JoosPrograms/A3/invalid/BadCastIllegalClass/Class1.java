package java.lang;
public class Class1 {
    public Class1 me = null;
    public Bad b = null;
    
    public Class1() {
        me = (Class1) b;
    }
}