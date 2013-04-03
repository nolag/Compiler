package java.lang;

public class Object{
    public Object() { }

    public void m1(){}

    public static int test(){
        Object o = new Object();
        if (o instanceof Object && !(o instanceof int[])){
            return 123;
        }else{
            return 0;
        }
    }
}
