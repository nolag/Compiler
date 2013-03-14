package java.lang;
public class BadAssignment {
    public BadAssignment me = null;
    public int i = 10;
    
    public BadAssignment() {
        i = me;
    }
}