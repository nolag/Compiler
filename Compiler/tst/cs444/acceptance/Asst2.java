package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class Asst2 {

	@Test
    public void testTypeCorrectJoosCode() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A2/TypeCorrectPrograms/", 0,  true);
    }

	@Ignore("Valid Marmoset tests are in A5 with code generated")
    @Test
    public void testMarmosetValidTestCases() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a2/valid/", 0, true);
    }

    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a2/invalid/", 42,  false);
    }
}
