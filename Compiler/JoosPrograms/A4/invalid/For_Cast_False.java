/**
 * Reachability:
 * - Check that all statements (including empty statements and empty
 * blocks) are reachable.  
 */
public class For_Cast_False {
    
    public For_Cast_False(){}

    public void test() {
    for (int i = 100; (short) (3/3) == (byte) (5+3*10); i=i+1) {
        i = i + 1;
    }
    }
}
