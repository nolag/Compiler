package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int flags = 0;
        boolean b = true;
        if (true || b) {
            flags = 1;
        }

        if (!b || !b) {
            flags = flags + 2;
        }

        if (false || b) {
            flags = flags + 4;
        }

        if (!b | !b) {
            flags = flags + 8;
        }

        return flags;
    }
}