package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Asst1 {

    @Test
    public void testValidJoosCode() throws IOException, InterruptedException {
        String path = TestHelper.TEST_LOCATION + "SyntacticallyValidPrograms/";
        TestHelper.assertReturnCodeForFiles(path, 0,  true);
    }

    @Test
    public void testInvalidJoosCode() throws IOException, InterruptedException {
        String path = TestHelper.TEST_LOCATION + "SyntacticallyInvalidPrograms/";

        TestHelper.assertReturnCodeForFiles(path, 42,  false);
    }

    @Test
    public void testMarmosetInvalidCases() throws IOException, InterruptedException {
        String path = TestHelper.TEST_LOCATION + "MarmosetPrograms/a1/invalid/";
        TestHelper.assertReturnCodeForFiles(path, 42, false);
    }
}
