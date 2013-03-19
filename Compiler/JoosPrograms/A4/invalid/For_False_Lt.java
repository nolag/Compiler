/**
 * Reachability:
 * - Check that all statements (including empty statements and empty
 * blocks) are reachable.  
 */
public class For_False_Lt {
    
    public For_False_Lt(){}

    public void test() {
    for (int i = 100; 3 > 10; i=i+1) {
        i = i + 1;
    }
    }
}
