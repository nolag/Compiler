package java.lang;
public class Inherits extends Base{
    public boolean i = false;
    public Inherits() { }
    
    public int getI(){
        return 1;
    }
    
    public int getMe(){
        return super.getI();
    }
}