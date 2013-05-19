package cs444.acceptance;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class A5 {

    /*public static void main(final String [] args) throws IOException, InterruptedException{
        final A5 a5 = new A5();
        a5.testCompileProgramsNoStdLib();
        a5.testCompileProgramsWithStdLib();
        a5.testMarmosetA1ValidTestCases();
        a5.testMarmosetA2ValidTestCases();
        a5.testMarmosetA3ValidTestCases();
        a5.testMarmosetA4ValidTestCases();
        a5.testMarmosetA5Programs();
    }*/

    @Test
    public void testCompileProgramsNoStdLib() throws IOException, InterruptedException{
        //SuperMethod is not implemented yet
        //List<String> ignoreList = new LinkedList<String>(Arrays.asList("SuperMethod"));
        final List<String> ignoreList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "A5/NoStdLibPrograms/", 0, true, false,
                true, ignoreList, new AsmAndLinkCallback());
    }

    /*@Test
    public void testCompileProgramsWithStdLib() throws IOException, InterruptedException{
        final List<String> ignoreList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "A5/WithStdLib/", 0, true, true,
                                            true, ignoreList, new AsmAndLinkCallback());
    }

    @Test
    public void testMarmosetA5Programs() throws IOException, InterruptedException{

        final List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a5/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    @Test
    public void testMarmosetA4ValidTestCases() throws IOException, InterruptedException {
        final List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a4/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    @Test
    public void testMarmosetA3ValidTestCases() throws IOException, InterruptedException {
        final List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a3/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    @Test
    public void testMarmosetA2ValidTestCases() throws IOException, InterruptedException {
        final List<String> failingList = new LinkedList<String>();

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a2/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }

    @Test
    public void testMarmosetA1ValidTestCases() throws IOException, InterruptedException {
        //J1w_Interface.java is an interface there is no test method
        final List<String> failingList = new LinkedList<String>(Arrays.asList("J1w_Interface.java"));

        TestHelper.assertReturnCodeForFiles(TestHelper.TEST_LOCATION + "MarmosetPrograms/a1/valid/", 0, true, true,
                true, failingList, new AsmAndLinkCallback());
    }*/
}

