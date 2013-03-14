package java.lang;
public class Inherits extends Base{
    public boolean i = false;
    public Inherits() { }
    
    public int getI(){
        return super.i;
    }
    
    public boolean getMe(){
        return i;
    }
}