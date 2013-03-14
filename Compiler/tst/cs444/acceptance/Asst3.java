package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import cs444.types.PkgClassInfo;

public class Asst3 {

    @Test
    public void testMarmosetValidTestCases() throws IOException, InterruptedException {
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/valid/", 0, true);
    }

    @Ignore("Implementation not working yet")
    @Test
    public void testMarmosetInvalidTestCases() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MarmosetPrograms/a3/invalid/", 42,  false);
    }

    @Test
    public void testAaa(){
        PkgClassInfo.instance.clear();
        System.out.println(cs444.Compiler.compile(new String []{"Object.java"}, true));
    }

    @Test
    public void a3Testing() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A3/valid/", 0, true, false);
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A3/invalid/", 42, false, false);
    }
}
