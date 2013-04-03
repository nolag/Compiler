package java.lang;

public class AClass{
    public BClass o;

    public AClass() {
        o = new BClass();
    }

    public int m1(){
        return o.i;
    }

    public static int test(){
        AClass a = new AClass();
        return a.m1();
    }
}
