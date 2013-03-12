package cs444.acceptance;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

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
        System.out.println(cs444.Compiler.compile(new String []{"Object.java"}, true));
    }
}
