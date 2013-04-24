package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class Asst1 {

    @Test
    public void testValidJoosCode() throws IOException, InterruptedException {
        String path = "JoosPrograms/SyntacticallyValidPrograms/";
        TestHelper.assertReturnCodeForFiles(path, 0,  true);
    }

    @Test
    public void testInvalidJoosCode() throws IOException, InterruptedException {
        String path = "JoosPrograms/SyntacticallyInvalidPrograms/";

        TestHelper.assertReturnCodeForFiles(path, 42,  false);
    }

    @Ignore("Valid Marmoset tests are in A5 with code generated")
    @Test
    public void testMarmosetValidCases() throws IOException, InterruptedException{
        String path = "JoosPrograms/MarmosetPrograms/a1/valid/";
        TestHelper.assertReturnCodeForFiles(path, 0,  true);
    }

    @Test
    public void testMarmosetInvalidCases() throws IOException, InterruptedException {
        String path = "JoosPrograms/MarmosetPrograms/a1/invalid/";
        TestHelper.assertReturnCodeForFiles(path, 42, false);
    }
}
