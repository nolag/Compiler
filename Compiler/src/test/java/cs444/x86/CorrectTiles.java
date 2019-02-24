package cs444.x86;

import cs444.Compiler;
import cs444.CompilerException;
import cs444.acceptance.TestHelper;
import cs444.codegen.Platform;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_64.X86_64Platform;
import cs444.types.PkgClassInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CorrectTiles {
    public static final String TILE_TESTS = TestHelper.TEST_LOCATION + "TileTests/";

    private static final Set<String> opts = Collections.emptySet();
    private static final Set<Platform<?, ?>> platforms = new HashSet<Platform<?, ?>>();

    static {
        platforms.add(X86_32Platform.Factory.FACTORY.getPlatform(opts));
        platforms.add(X86_64Platform.Factory.FACTORY.getPlatform(opts));
    }

    private static void deleteNonRuntime(File root) {
        for (File child : root.listFiles()) {
            if (child.getName().equals("runtime.s") || child.getName().equals("runtime.asm")) {
                continue;
            }
            if (child.isDirectory()) {
                deleteNonRuntime(child);
                continue;
            }
            if (!child.delete()) {
                System.err.println("Couldn't delete file: " + child.getAbsolutePath());
            }
        }
    }

    public void setup() {
        PkgClassInfo.instance.clear();
        deleteNonRuntime(new File(Compiler.OUTPUT_DIRECTORY));
    }

    public boolean compileAndContains(String str, String... files) throws IOException {
        setup();
        Compiler.compile(Arrays.asList(files), true, true, platforms);

        return contains(str, files);
    }

    public boolean contains(String str, String... files) throws IOException {
        boolean hasSoFar = false;
        platforms:
        for (Platform<?, ?> platform : platforms) {
            File folder = new File(platform.getOutputDir());
            for (File file : folder.listFiles()) {
                if (file.getName().endsWith("runtime.s") || file.getName().startsWith("_")) {
                    continue;
                }
                if (file.isDirectory()) {
                    continue;
                }
                byte[] b = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(b);
                fis.close();
                String s = new String(b);
                if (s.contains(str)) {
                    hasSoFar = true;
                    continue platforms;
                }
            }
            //One platform has it and others don't this is bad.
            assertFalse(hasSoFar);
            return false;
        }
        return true;
    }

    @Test
    public void finalMethodTest() throws CompilerException, IOException {
        String path = TILE_TESTS + "NonStaticFinalCall/Object.java";
        assertTrue(compileAndContains("call Fn$java.lang.Object.unique$$this$int$", path));
    }

    @Test
    public void divZeroTest() throws CompilerException, IOException {
        String path = TILE_TESTS + "DivZero/Object.java";
        assertFalse(compileAndContains("div", path));
    }

    @Test
    public void remZeroTest() throws CompilerException, IOException {
        String path = TILE_TESTS + "RemZero/Object.java";
        assertFalse(compileAndContains("div", path));
    }

    @Test
    public void mulZeroTest() throws CompilerException, IOException {
        String path = TILE_TESTS + "MulZero/Object.java";
        assertFalse(compileAndContains("mul", path));
    }

    @Test
    public void superNew() throws CompilerException, IOException {
        String superBase = TILE_TESTS + "SuperField/";
        String[] superPaths = {superBase + "Base.java", superBase + "Object.java", superBase + "Super.java"};
        assertFalse(compileAndContains("je ", superPaths));
    }

    @Test
    public void superNew2() throws CompilerException, IOException {
        String path = TILE_TESTS + "NonStaticCall2/Object.java";
        assertFalse(compileAndContains("je ", path));
    }

    @Test
    public void upCast() throws CompilerException, IOException {
        String superBase = TILE_TESTS + "UpCast/";
        String[] superPaths = {superBase + "Base.java", superBase + "Object.java", superBase + "Super.java"};
        assertTrue(compileAndContains("Up cast to", superPaths));
    }

    //The tests from here on are not tile tests, but make sure the ISymbols worked.

    @Test
    public void addZero() throws CompilerException, IOException {
        String addBase = TILE_TESTS + "AddZero/";
        String[] superPaths = {addBase + "Object.java", addBase + "String.java"};
        assertFalse(compileAndContains(", 0", superPaths));
    }

    @Test
    public void subZero() throws CompilerException, IOException {
        String subBase = TILE_TESTS + "SubZero/";
        String[] superPaths = {subBase + "Object.java"};
        assertFalse(compileAndContains("sub", superPaths));
        assertTrue(contains("neg ", superPaths));
    }

    @Test
    public void multDivPow2() throws CompilerException, IOException {
        String subBase = TILE_TESTS + "MultDivPow2/";
        String[] superPaths = {subBase + "Object.java"};
        assertFalse(compileAndContains("mul ", superPaths));
        //IDiv cannot be replaced by sar because they round differently for -ve numbers
        //assertFalse(contains("div", superPaths));
    }

    @Test
    //Note that or is similar and the xor instruciton is used so it was hard to test as well.  And will do for both.
    public void andReduce() throws CompilerException, IOException {
        String subBase = TILE_TESTS + "AndOpts/";
        String[] superPaths = {subBase + "Object.java"};
        assertFalse(compileAndContains("Start Assignment Simple Name=And", superPaths));
    }

    @Test
    public void negReducing() throws CompilerException, IOException {
        String subBase = TILE_TESTS + "NegNegNegNegOpt/";
        String[] superPaths = {subBase + "Object.java"};
        assertFalse(compileAndContains("neg", superPaths));
    }

    @Test
    public void constantSimplify() throws CompilerException, IOException {
        String subBase = TILE_TESTS + "Literals";
        for (int i = 1; i < 6; i++) {
            String numberedBase = subBase + i + "/";
            String[] superPaths = {numberedBase + "Object.java", subBase + "String.java"};
            assertFalse(compileAndContains("eax, ecx", superPaths));
            assertFalse(contains("rax, rcx", superPaths));
        }
    }
}
