package cs444.tiles.x86;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import cs444.Compiler;
import cs444.CompilerException;
import cs444.acceptance.TestHelper;

public class CorrectTiles {
    public static final String TILE_TESTS = TestHelper.TEST_LOCATION + "TileTests/";

    @Test
    public void finalMethodTest() throws CompilerException, IOException{
        final String path =  TILE_TESTS + "NonStaticFinalCall/Object.java";
        final String [] files = new String [] {path};
        Compiler.compile(files, true, true);
        final File file = new File(Compiler.OUTPUT_DIRECTORY + "java.lang.Object.s");
        final byte[] b = new byte[(int) file.length()];
        final FileInputStream fis = new FileInputStream(file);
        fis.read(b);
        fis.close();
        final String s = new String(b);
        assertTrue(s.contains("call java.lang.Object.getI"));
    }
}
