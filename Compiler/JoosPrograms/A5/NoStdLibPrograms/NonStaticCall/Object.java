package java.lang;

public class Object{
    public int i = 0;
    
    protected int getI(){
        return this.i;
    }
    
    public Object(int j) {
        this.i = j;
    }
    
    public static int test(){
        Object o = new Object(10);
        return o.getI();
    }
}