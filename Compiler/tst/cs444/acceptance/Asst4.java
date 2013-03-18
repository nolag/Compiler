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
        List<String> ignoreList = new LinkedList<String>(Arrays.asList(
                "Je_7_Reachability_AfterElseReturn.java",
                "Je_7_Reachability_AfterElseReturn.java",
                "Je_7_Reachability_AfterIfReturn.java",
                "Je_7_Reachability_AfterIfReturnElseReturn.java",
                "Je_7_Reachability_AfterReturnEmptyBlock.java",
                "Je_7_Reachability_AfterReturn_Constructor.java",
                "Je_7_Reachability_AfterValueReturn.java",
                "Je_7_Reachability_AfterVoidReturn.java",
                "Je_7_Reachability_EmptyValueMethod.java",
                "Je_7_Reachability_ForFalse_1.java",
                "Je_7_Reachability_ForFalse_2.java",
                "Je_7_Reachability_ReturnReturn.java",
                "Je_7_Reachability_WhileFalse_ConstantFolding.java",
                "Je_7_Reachability_WhileFalse_Empty.java",
                "Je_7_Reachability_WhileFalse_IfThenElse.java",
                "Je_7_Reachability_WhileTrue.java",
                "Je_7_Reachability_WhileTrue_ConstantFolding.java",
                "Je_7_Return_IfElseIf.java",
                "Je_7_Return_IfIfNoElseElse.java",
                "Je_7_Return_IfIfNot.java",
                "Je_7_Return_MissingInElse.java"));

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a4/invalid/", 42,  false, ignoreList);
    }
}