package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        AClass aclass = new AClass();

        aclass.fChar = (char) 10;
        aclass.fInt = aclass.fShort = (short)aclass.fChar;
        return aclass.fInt * aclass.fChar;
    }
}
