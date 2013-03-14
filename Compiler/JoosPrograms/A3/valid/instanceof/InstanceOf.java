package java.lang;
public class InstanceOf {
    public InstanceOf() { }
    
   public boolean isInstance(InstanceOf i){
        return i instanceof InstanceOf;
    }
    
    public boolean isInstance(){
        return this instanceof InstanceOf && null instanceof InstanceOf;
    }
}