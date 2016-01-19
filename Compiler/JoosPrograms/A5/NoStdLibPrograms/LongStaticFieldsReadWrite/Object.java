package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        AClass.fLong = AClass.fChar = (char) 2;
        return AClass.fChar * AClass.fShort * (int)AClass.fLong * (int)AClass.fClass.aLong();
    }
}
