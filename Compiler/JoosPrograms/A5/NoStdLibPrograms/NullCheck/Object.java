package java.lang;

public class Object{
    public static Object o;

    public Object() { }

    public void m1(){

    }

    public static int test(){
        Object.o.m1(); // NullPointerException 
        return -1;
    }
}
