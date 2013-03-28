package cs444.acceptance;

import java.io.IOException;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

public class A5 {

    @Test
    public void testCompileProgramsNoStdLib() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/A5/NoStdLibPrograms/", 0, true, false,
                true, Collections.<String>emptyList(), new AsmAndLinkCallback());
    }

    @Ignore("Not ready")
    @Test
    public void testMarmosetPrograms() throws IOException, InterruptedException{
        TestHelper.assertReturnCodeForFiles("JoosPrograms/MArmosetPrograms/a5/", 0, true, true,
                true, Collections.<String>emptyList(), new AsmAndLinkCallback());
    }
}

