package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cs444.types.PkgClassInfo;

public class Asst3 {

    @Test
    public void testMarmosetValidTestCases() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/valid/", 0, true);
    }

    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        List<String> ignoreList = new LinkedList<String>(Arrays.asList(
                "Je_16_StaticThis_StaticFieldInitializer.java",
                "Je_1_Dot_ParenthesizedType_Field.java",
                "Je_5_ForwardReference_ArrayLength.java",
                "Je_5_ForwardReference_FieldDeclaredLater.java",
                "Je_5_ForwardReference_FieldDeclaredLater_ComplexExp.java",
                "Je_5_ForwardReference_FieldInOwnInitializer_ComplexExpression.java",
                "Je_5_ForwardReference_FieldInOwnInitializer_Direct.java",
                "Je_5_ForwardReference_FieldInOwnInitializer_ReadAfterAssignment.java",
                "Je_5_ForwardReference_FieldInOwnInitializer_RightSideOfAssignment.java",
                "Je_6_Assignable_ToSubtype_FieldInit.java"));

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/invalid/", 42,  false, ignoreList);
    }

    @Test
    public void testAaa(){
        PkgClassInfo.instance.clear();
        System.out.println(cs444.Compiler.compile(new String []{"Object.java"}, true, false));
    }

    @Test
    public void a3Testing() throws IOException, InterruptedException{
        List<String> ignoreList = new LinkedList<String>(Arrays.asList("IfWhileFor"));
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A3/valid/", 0, true, false, ignoreList);
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A3/invalid/", 42, false, false,ignoreList);
    }
}
