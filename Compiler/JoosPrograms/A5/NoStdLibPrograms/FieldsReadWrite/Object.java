package java.lang;

public class Object{
    public int fInt = 2;

    public Object() { }
    public static int test(){
        AClass aclass = new AClass();
        Object obj = new Object();

        aclass.fChar = (char) 2;
        aclass.fInt = aclass.fShort = (short)aclass.fChar;

        return aclass.fInt * aclass.fChar * obj.fInt;
    }
}
