package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class A5 {
    @Test
    public void testCompileProgramsNoStdLib() throws IOException, InterruptedException{
        List<String> ignoreList = new LinkedList<String>(
                Arrays.asList(
                        "ImplicitClassNameForStaticFields",
                        "NullCheck"
                        )
                );

        TestHelper.assertReturnCodeForFiles("JoosPrograms/A5/NoStdLibPrograms/", 0, true, false,
                true, ignoreList, new AsmAndLinkCallback());
    }

    //@Ignore("not ready")
    @Test
    public void testMarmosetPrograms() throws IOException, InterruptedException{

        List<String> failingList = new LinkedList<String>(Arrays.asList(
                "J1_Hello",
                "J1_implicitstringconcatenation",
                "J1_sideeffects_array2",
                "J1_sideeffects_obj",
                "J1_sideeffects_obj3",
                "J1_sim_xor"
                ));

        Map<String, String> env = System.getenv();
        String allTest = env.get("all_tests");
        if (allTest != null && allTest.equals("true")){
            TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a5/", 0, true, true,
                    true, Collections.<String>emptyList(), new AsmAndLinkCallback());
        } else{
            TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a5/", 0, true, true,
                    true, failingList, new AsmAndLinkCallback());
        }
    }
}

