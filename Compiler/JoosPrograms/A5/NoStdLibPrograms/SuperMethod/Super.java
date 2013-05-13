package java.lang;

public class Super{
    public Super(){ }
    public int getValue(){
        return 1;
    }
    
    public int helper(){
        return super.getValue();
    }
    
    public int helper2(){
        return  super.getObj().getValue() - 1;
    }
    
    public int arg(){
        return super.retVal(getValue()) - 1;
    }
    
    public static int test(){
        Super s = new Super();
        return s.helper() + s.helper2() + s.arg();
    }
}