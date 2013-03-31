package java.lang;

public class Object{
    public int fInt2 = 4;

    public Object() { }
    public static int test(){
        AClass aclass = new AClass();
        Object obj = new Object();

        aclass.fChar = (char) 2;
        aclass.fInt = aclass.fShort = (short)aclass.fChar;
        obj.fInt2 = 3;

        return aclass.fInt2 * aclass.fInt * obj.fInt2; // 4 * 2 * 3
    }
}
