package java.lang;
public class ValidReturns {
    public ValidReturns() { 
        return;
    }
    
    public int a(){
        return a();
    }
    
    public char c(){
        return 'z';
    }
    
    public ValidReturns vr(){
        return this;
    }
    
    public ValidReturns nullMe(){
        return null;
    }
    
    public void x(){
        int i = 10;
        return;
    }
}