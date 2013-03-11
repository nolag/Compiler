package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class Asst3 {

    @Ignore("Implementation not working yet")
    @Test
    public void testMarmosetValidTestCases() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/valid/", 0, false);
    }

    @Ignore("Implementation not working yet")
    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/invalid/", 42,  false);
    }
}
