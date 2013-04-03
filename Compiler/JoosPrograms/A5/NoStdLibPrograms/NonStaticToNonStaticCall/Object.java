package java.lang;

public class Object{
    public Object o = new Object();

    public Object() { }

    public int m2(){
        return 123;
    }

    public int m1(){
        return o.m2();
    }

    public static int test(){
        Object obj = new Object();
        return obj.m1();
    }
}
