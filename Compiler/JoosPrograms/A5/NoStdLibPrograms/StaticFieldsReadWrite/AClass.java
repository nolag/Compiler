package java.lang;

public class AClass{
    public static int fInt = 2;
    public static short fShort = (short) 3;
    public static char fChar = (char) 4;
    public static boolean fBool;
    public static AClass fClass = new AClass();
    public static int fInt2 = new AClass().anInt();

    public AClass() { }

    public int anInt(){
        return 4;
    }
}