package java.lang;
public class MethodCalls {
    public MethodCalls me = new MethodCalls();
    
    public MethodCalls() { }
    
    public int getMyVal(int i){
        return i;
    }
    
    public int getMyVal(int i, int j){
        return i;
    }
    
    public boolean getMyVal(boolean b){
        return b;
    }
    
    public MethodCalls mc(){
        return me;
    }
    
    public int getIntVal(){
        return me.getMyVal(10);
    }
    
    public int getIntVal(int i){
        return me.getMyVal(i, 10);
    }
    
    public boolean getIntVal(boolean b){
        return mc().getMyVal(getMyVal(b));
    }
}