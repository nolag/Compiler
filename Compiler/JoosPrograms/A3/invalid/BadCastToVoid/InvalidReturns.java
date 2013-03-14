package java.lang;
public class InvalidReturns {
    public InvalidReturns() { 
        return;
    }
    
    public void nullMe(){
        return (void) this;
    }
    
}