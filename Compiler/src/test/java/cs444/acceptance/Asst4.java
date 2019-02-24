package cs444.acceptance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class Asst4 {

    @Test
    public void testA4Valid() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "A4/valid/", 0, true);
    }

    @Test
    public void testA4Invalid() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "A4/invalid/", 42, false);
    }

    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a4/invalid/", 42, false);
    }
}
