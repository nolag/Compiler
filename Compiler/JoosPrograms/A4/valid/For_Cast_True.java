/**
 * Reachability:
 * - Check that all statements (including empty statements and empty
 * blocks) are reachable.  
 */
public class For_Cast_True {
    
    public For_Cast_True(){}

    public void test() {
    for (int i = 100; (char) ((3 + 2) * (byte)(14 % 3)) == (char) (20 - 100 / 10); i=i+1) {
        i = i + 1;
    }
    }
}
