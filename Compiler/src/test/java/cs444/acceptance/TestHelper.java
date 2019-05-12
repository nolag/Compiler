package cs444.acceptance;

import cs444.Compiler;
import cs444.CompilerSettings;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassInfo;
import cs444.types.PkgClassResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;

public class TestHelper {

    public static final String TEST_LOCATION = Compiler.BASE_DIRECTORY + "JoosPrograms/";
    // NOTE: this needs to change pending on the platform you are running/testing on.
    public static final String[] testPlatforms = {"-x64", "-x86", "-a32"};
    private static ITestCallbacks callbacks;
    private static boolean outputAsmFiles;
    private static Set<Platform<?, ?>> platforms;

    public static void assertReturnCodeForFiles(String path, int expectedReturnCode,
                                                boolean printErrors,
                                                boolean includeStdLib, boolean outputAsmFiles,
                                                List<String> ignoreList, ITestCallbacks testCallbacks)
            throws IOException, InterruptedException {

        callbacks = testCallbacks;
        TestHelper.outputAsmFiles = outputAsmFiles;

        File folder = new File(path);

        int totalTests = 0;
        int filesSkipped = 0;
        List<String> failFiles = new ArrayList<String>();
        for (File file : folder.listFiles()) {
            String fileName = file.getName();

            if (ignoreList.contains(fileName)) {
                System.out.print("*"); // skip file
                filesSkipped++;
                continue;
            }

            if (file.isFile() && fileName.toLowerCase().endsWith(".java")
                    || (file.isDirectory() && !fileName.toLowerCase().endsWith(".skip"))) {
                runTestCase(path, expectedReturnCode, printErrors, includeStdLib, failFiles, file, fileName);
                totalTests++;
            } else {
                System.out.print("*"); // skip file
                filesSkipped++;
            }
        }

        printSummary(totalTests, filesSkipped, failFiles);
        int failures = failFiles.size();
        assertEquals("Unexpected return code compiling or running " + failures + " files. Expected return code was: " + expectedReturnCode,
                0, failures);
    }

    private static void runTestCase(String path, int expectedReturnCode, boolean printErrors,
                                    boolean includeStdLib, List<String> failFiles, File file,
                                    String fileName) throws IOException,
            InterruptedException {
        List<String> sourceFiles = getAllFiles(file);

        if (!(callbacks.beforeCompile(file) && compileAndTest(sourceFiles, printErrors, includeStdLib) == expectedReturnCode && callbacks
                .afterCompile(file, platforms))) {
            failFiles.add(path + fileName);
        }
    }

    public static void assertReturnCodeForFiles(String path, int expectedReturnCode,
                                                boolean printErrors,
                                                boolean includeStdLib, List<String> ignoreList) throws IOException,
            InterruptedException {
        assertReturnCodeForFiles(path, expectedReturnCode, printErrors, includeStdLib, false, ignoreList,
                new EmptyCallbacks());
    }

    public static void assertReturnCodeForFiles(String path, int expectedReturnCode,
                                                boolean printErrors)
            throws IOException, InterruptedException {
        assertReturnCodeForFiles(path, expectedReturnCode, printErrors, true);
    }

    public static void assertReturnCodeForFiles(String path, int expectedReturnCode,
                                                boolean printErrors,
                                                boolean includeStdLib) throws IOException, InterruptedException {
        assertReturnCodeForFiles(path, expectedReturnCode, printErrors, includeStdLib, Collections.emptyList());
    }

    public static void assertReturnCodeForFiles(String path, int expectedReturnCode,
                                                boolean printErrors,
                                                List<String> ignoreList) throws IOException,
            InterruptedException {
        assertReturnCodeForFiles(path, expectedReturnCode, printErrors, true, ignoreList);
    }

    private static List<String> getAllFiles(File root) {

        ArrayList<String> result = new ArrayList<>();
        Stack<File> toVisit = new Stack<>();

        toVisit.push(root);

        while (!toVisit.isEmpty()) {
            File currentFile = toVisit.pop();
            if (currentFile.isFile()) {
                String fileName = currentFile.getAbsolutePath();
                if (fileName.endsWith(".java")) {
                    if (fileName.endsWith("Main.java")) {
                        result.add(0, fileName);
                    } else {
                        result.add(fileName);
                    }
                }
            } else if (currentFile.isDirectory()) {
                for (File sourceFile : currentFile.listFiles()) {
                    toVisit.push(sourceFile);
                }
            }
        }

        return result;
    }

    private static void printSummary(int totalTests, int filesSkipped, List<String> failFiles) {
        System.out.println("\nNumber of tests: " + totalTests);
        if (filesSkipped > 0) {
            System.out.println("Number of files skipped: " + filesSkipped);
        }
        if (failFiles.size() != 0) {
            System.out.println("Failed " + failFiles.size());
            for (String fileName : failFiles) {
                System.out.println("\t" + fileName);
            }
        }
    }

    private static int compileAndTest(List<String> files, boolean printErrors, boolean includeStdlib) {
        PkgClassResolver.reset();
        TileSet.reset();
        CodeGenVisitor.reset();

        if (includeStdlib) {
            files.addAll(getAllFiles(new File(TEST_LOCATION + "StdLib")));
        }

        PkgClassInfo.instance.clear();
        Set<String> opts = Collections.emptySet();
        platforms = new HashSet<>();
        for (String platformStr : testPlatforms) {
            platforms.add(CompilerSettings.platformMap.get(platformStr).getPlatform(opts));
        }

        return Compiler.compile(files, printErrors, outputAsmFiles, platforms);
    }
}
