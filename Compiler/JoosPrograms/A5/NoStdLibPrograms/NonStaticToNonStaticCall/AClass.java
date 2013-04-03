package java.lang;

public class AClass{
    public Object o = new Object();

    public AClass() { }

    public int m1(){
        return o.m2();
    }

    public static int test(){
        AClass obj = new AClass();
        return obj.m1();
    }
}