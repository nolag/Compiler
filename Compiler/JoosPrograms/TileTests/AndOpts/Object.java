package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int flags = 0;
        boolean b = true;
        if (true && b) {
            flags = 1;
        }

        if (b && true) {
            flags = flags + 2;
        }

        if (!b && !b) {
            flags = flags + 4;
        }

        if (false && b) {
            flags = flags + 8;
        }

        if (b && false) {
            flags = flags + 16;
        }

        if (!b & !b) {
            flags = flags + 32;
        }

        return flags;
    }
}