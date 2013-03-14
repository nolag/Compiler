package java.lang;
public class CorrectAssignmentsAndCasts2  extends CorrectAssignmentsAndCasts{
    public CorrectAssignmentsAndCasts me = null;
    public CorrectAssignmentsAndCasts2 me2 = null;
    
    public CorrectAssignmentsAndCasts2() {
        me = null;
        me2 = this;
        me2 = (CorrectAssignmentsAndCasts2) me;
        me = (CorrectAssignmentsAndCasts) me2;
    }
}