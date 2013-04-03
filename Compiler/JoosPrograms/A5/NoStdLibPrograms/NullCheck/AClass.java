package java.lang;

public class AClass{
    public static Object o;

    public AClass() { }

    public static int test(){
        AClass.o.m1(); // NullPointerException 
        return -1;
    }
}