package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        AClass.fInt = AClass.fChar = (char) 10;
        return AClass.fChar * AClass.fInt;
    }
}
