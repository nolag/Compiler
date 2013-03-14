package java.lang;
public class BadCastToVariable {
    public int i = 10;
    public boolean j = false;
    public BadCastToVariable me = null;
    public short s = (short) 10;
    public BadCastToVariable() {
        i = 100;
        s = (short) i;
        me = this;
        me = null;
    }
}