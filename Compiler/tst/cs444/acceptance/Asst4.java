package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class Asst4 {

    @Test
    public void testMarmosetValidTestCases() throws IOException, InterruptedException {
        List<String> ignoreList = new LinkedList<String>(Arrays.asList(
                "J1_7_Reachability_WhileTrue_ConstantFolding.java"));

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a4/valid/", 0, true, ignoreList);
    }

    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        List<String> ignoreList = new LinkedList<String>(Arrays.asList(
                "Je_7_Reachability_ForFalse_1.java",
                "Je_7_Reachability_WhileFalse_ConstantFolding.java",
                "Je_7_Reachability_WhileTrue_ConstantFolding.java"));

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a4/invalid/", 42,  false, ignoreList);
    }
}
