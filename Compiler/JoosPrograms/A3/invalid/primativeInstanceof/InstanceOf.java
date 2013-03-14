package java.lang;
public class InstanceOf {
    public InstanceOf() { }
    
    public boolean isInstance(){
        int i = 10;
        return i instanceof InstanceOf;
    }
}