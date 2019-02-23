package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Asst2 {

	@Test
    public void testTypeCorrectJoosCode() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "A2/TypeCorrectPrograms/", 0,  true);
    }

    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a2/invalid/", 42,  false);
    }
}
