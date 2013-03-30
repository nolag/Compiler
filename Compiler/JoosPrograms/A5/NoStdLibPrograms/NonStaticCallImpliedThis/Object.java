package java.lang;

public class Object{
    public int i = 0;
    
    public int getMyI(){
        return i;
    }
    
    protected int getI(){
        return getMyI();
    }
    
    public Object(int j) {
        this.i = j;
    }
    
    public static int test(){
        Object o = new Object(10);
        Object i = new Object(9);
        return o.getI();
    }
}