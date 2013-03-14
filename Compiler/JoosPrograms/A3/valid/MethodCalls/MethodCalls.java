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
    
    public int getVal(){
        return me.getVal(10);
    }
    
    public int getVal(int i){
        return me.getVal();
    }
    
    public boolean getVal(boolean b){
        return mc().getVal(getVal(b));
    }
    
    public boolean getVal(boolean [] b, int i){
        return mc().getVal(getVal(b, i));
    }
}