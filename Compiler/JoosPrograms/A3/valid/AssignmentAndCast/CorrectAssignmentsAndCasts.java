package java.lang;
public class CorrectAssignmentsAndCasts {
    public int i = 10;
    public boolean j = false;
    public CorrectAssignmentsAndCasts me = null;
    //public short s = (short) 10;
    
    public CorrectAssignmentsAndCasts() {
        i = 100;
        //s = (short) i;
        CorrectAssignmentsAndCasts = this;
        CorrectAssignmentsAndCasts = null;
    }
}