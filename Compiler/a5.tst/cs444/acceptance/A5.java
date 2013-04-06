package cs444.acceptance;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    @Test
    public void testMarmosetA5Programs() throws IOException, InterruptedException{

        List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a5/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    //@Ignore("Ignoring previous tests")
    @Test
    public void testMarmosetA4ValidTestCases() throws IOException, InterruptedException {
        List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a4/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    //@Ignore("Ignoring previous tests")
    @Test
    public void testMarmosetA3ValidTestCases() throws IOException, InterruptedException {
        List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    //@Ignore("Ignoring previous tests")
    @Test
    public void testMarmosetA2ValidTestCases() throws IOException, InterruptedException {
        List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a2/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    //@Ignore("Ignoring previous tests")
    @Test
    public void testMarmosetA1ValidTestCases() throws IOException, InterruptedException {
        List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a1/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }
}

