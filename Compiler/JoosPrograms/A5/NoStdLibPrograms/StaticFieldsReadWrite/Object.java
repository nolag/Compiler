package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        AClass.fInt = AClass.fChar = (char) 2;
        return AClass.fChar * AClass.fShort * AClass.fInt;
    }
}
