package java.lang;

public class Object{
    public int i = 0;
    public Object o;
    
    protected int getI(){
        return this.i;
    }

    public Object getMe(int j){
        i = j;
        return this;
    }
    
    public Object setO(Object o){
        return this.o = o;
    }
    
    public Object(int j) {
        this.i = j;
    }

    public Object(){}

    public static int test(){
        Object o = new Object(9);
        Object i = new Object(0);
        return o.getMe(10).getI() + o.getMe(9).getI() + o.setO(i).setO(i).o.o.getI();
    }
}
