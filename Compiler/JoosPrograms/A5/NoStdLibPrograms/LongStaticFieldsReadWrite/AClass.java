package java.lang;

public class AClass{
    public static long fLong = 2;
    public static short fShort = (short) 3;
    public static char fChar = (char) 4;
    public static boolean fBool;
    public static AClass fClass = new AClass();
    public static long fLong2 = new AClass().aLong();

    public AClass() { }

    public long aLong(){
        return 4;
    }
}