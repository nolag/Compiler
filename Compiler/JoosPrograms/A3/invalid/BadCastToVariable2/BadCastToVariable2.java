package java.lang;
public class BadCastToVariable2  extends CorrectAssignmentsAndCasts{
    public BadCastToVariable me = null;
    public BadCastToVariable2 me2 = null;
    
    public BadCastToVariable2() {
        me = null;
        me2 = this;
        me2 = (me2) me;
        me = me2;
    }
}