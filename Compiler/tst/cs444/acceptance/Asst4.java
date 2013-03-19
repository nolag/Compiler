package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class Asst4 {

    @Test
    public void testMarmosetValidTestCases() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a4/valid/", 0, true);
    }

    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a4/invalid/", 42,  false);
    }
}
