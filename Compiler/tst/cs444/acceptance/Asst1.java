package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class Asst1 {

    @Test
    public void testValidJoosCode() throws IOException, InterruptedException {
        String path = "JoosPrograms/SyntacticallyValidPrograms/";

        List<String> ignoreList = new LinkedList<String>(Arrays.asList(
                "WhileNestedIfElse.java"));

        TestHelper.assertReturnCodeForFiles(path, 0,  true, ignoreList);
    }

    @Test
    public void testInvalidJoosCode() throws IOException, InterruptedException {
        String path = "JoosPrograms/SyntacticallyInvalidPrograms/";

        TestHelper.assertReturnCodeForFiles(path, 42,  false);
    }

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
