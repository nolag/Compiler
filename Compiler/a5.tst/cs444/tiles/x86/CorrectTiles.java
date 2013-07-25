package cs444.tiles.x86;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import cs444.Compiler;
import cs444.CompilerException;
import cs444.acceptance.TestHelper;
import cs444.codegen.x86.x86_32.linux.X86_32LinuxPlatform;
import cs444.types.PkgClassInfo;

public class CorrectTiles {
    public static final String TILE_TESTS = TestHelper.TEST_LOCATION + "TileTests/";

    public void setup(){
        final Map<String, Boolean> opts = Collections.emptyMap();
        X86_32LinuxPlatform.reset(opts);
        PkgClassInfo.instance.clear();
        final File folder = new File(Compiler.OUTPUT_DIRECTORY);
        for(final File file : folder.listFiles()){
            if(file.getName().endsWith("runtime.s")) continue;
            file.delete();
        }
    }

    public boolean contains(final String str, final String ... files) throws IOException{
        setup();
        Compiler.compile(files, true, true);
        final File folder = new File(Compiler.OUTPUT_DIRECTORY);
        for(final File file : folder.listFiles()){
            if(file.getName().endsWith("runtime.s") || file.getName().startsWith("_")) continue;
            final byte[] b = new byte[(int) file.length()];
            final FileInputStream fis = new FileInputStream(file);
            fis.read(b);
            fis.close();
            final String s = new String(b);
            if(s.contains(str)){
                return true;
            }
        }
        return false;
    }

    @Test
    public void finalMethodTest() throws CompilerException, IOException{
        final String path =  TILE_TESTS + "NonStaticFinalCall/Object.java";
        assertTrue(contains("call java.lang.Object.getI", path));
    }

    @Test
    public void divZeroTest() throws CompilerException, IOException{
        final String path =  TILE_TESTS + "DivZero/Object.java";
        assertFalse(contains("div", path));
    }

    @Test
    public void mulZeroTest() throws CompilerException, IOException{
        final String path =  TILE_TESTS + "MulZero/Object.java";
        assertFalse(contains("mul", path));
    }

    @Test
    public void superNew() throws CompilerException, IOException{
        final String superBase = TILE_TESTS + "SuperField/";
        final String [] superPaths = {superBase +  "Base.java", superBase + "Object.java", superBase + "Super.java"};
        assertFalse(contains("je ", superPaths));
    }

    @Test
    public void superNew2() throws CompilerException, IOException{
        final String path =  TILE_TESTS + "NonStaticCall2/Object.java";
        assertFalse(contains("je ", path));
    }

    @Test
    public void upCast() throws CompilerException, IOException{
        final String superBase = TILE_TESTS + "UpCast/";
        final String [] superPaths = {superBase +  "Base.java", superBase + "Object.java", superBase + "Super.java"};
        assertTrue(contains("Up cast to", superPaths));
    }
}
