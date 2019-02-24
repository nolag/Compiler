package cs444.acceptance;

import cs444.Compiler;
import cs444.codegen.OperatingSystem;
import cs444.codegen.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

public class AsmAndLinkCallback implements ITestCallbacks {
    //NOTE: change this if you are testing on another os or if you change the exec/link to work multi os
    // private static final String osToTest = "linux";
    private static final String osToTest = "osx";

    private static final String EXEC = "main";
    private static final int EXPECTED_DEFAULT_RTN_CODE = 123;

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

    //TODO this class's stuff can be in parallel, check if it's worth it.

    private static boolean isAlive(Process p) {
        try {
            p.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }

    @Override
    public boolean beforeCompile(File file) throws IOException {
        File outputDir = new File(Compiler.OUTPUT_DIRECTORY);

        for (File platformFolder : outputDir.listFiles()) {
            //some computers (my mac) make files like .DS_Store...
            if (platformFolder.isFile()) {
                continue;
            }
            deleteNonRuntime(platformFolder);
        }

        return true;
    }

    @Override
    public boolean afterCompile(File file, Collection<Platform<?, ?>> platforms) throws IOException,
            InterruptedException {

        String stdOut = getExpectedOutput(file, true);
        String stdErr = getExpectedOutput(file, false);

        int expectedReturnCode = getExpectedReturnCode(file);

        for (Platform<?, ?> platform : platforms) {
            OperatingSystem<?> os = null;

            for (OperatingSystem<?> tmp : platform.getOperatingSystems()) {
                if (tmp.name.equals(osToTest)) {
                    os = tmp;
                    break;
                }
            }

            File folder = new File(platform.getOutputDir());
            if (!assembleOutput(folder, os)) {
                return false;
            }

            String[] link = os.getLinkCmd(EXEC);
            if (execAndWait(link, null, null) != 0) {
                System.out.println("********link failed!********");
                for (String part : link) {
                    System.out.print(part + " ");
                }
                System.out.println();
                return false;
            }

            String[] command = os.getExecuteCmd(EXEC);

            int returnCode = execAndWait(command, stdOut, stdErr);

            if (expectedReturnCode != returnCode) {
                System.out.println("\nWrong return code " + returnCode + " expected: " + expectedReturnCode + " on " +
                        "platform "
                        + platform.getClass());
                System.out.println("In: " + file);
                return false;
            }
        }
        return true;
    }

    private int getExpectedReturnCode(File file) {
        File returnCodeFile = new File(file, "return.code");
        int expectedReturnCode = EXPECTED_DEFAULT_RTN_CODE;

        if (!returnCodeFile.exists()) {
            return EXPECTED_DEFAULT_RTN_CODE;
        }

        try (Scanner scan = new Scanner(returnCodeFile)) {
            String line = scan.nextLine();
            expectedReturnCode = Integer.parseInt(line);
        } catch (FileNotFoundException e) { /*should never get here */}

        return expectedReturnCode;
    }

    private String getExpectedOutput(File file, boolean output) {
        File outputFile = new File(file, output ? "out.txt" : "err.txt");
        if (!outputFile.exists()) {
            return "";
        }
        try (Scanner scanner = new Scanner(outputFile)) {
            //Don't care about \r for testing because new lines may not match up the same for all systems.  Assume
            // that the runtime is correct.
            return scanner.useDelimiter("\\A").next().replace("\r", "");
        } catch (FileNotFoundException e) {
            //Should not get here
            e.printStackTrace();
            return "";
        }
    }

    private boolean assembleOutput(File folder, OperatingSystem<?> os) throws IOException,
            InterruptedException {

        if (os == null) {
            System.out.println("No operating system, skipping os!");
            return true;
        }

        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();
            if (!fileName.endsWith(".s")) {
                continue;
            }
            String[] command = os.getAssembleCmd(file);
            if (execAndWait(command, null, null) != 0) {
                return false;
            }
        }

        File runtime = os.getRuntimeFile();
        String[] command = os.getAssembleCmd(runtime);
        return execAndWait(command, null, null) == 0;
    }

    private int execAndWait(String[] command, String out, String err) throws InterruptedException,
            IOException {
        Process proc = Runtime.getRuntime().exec(command);
        StringBuilder commandStr = new StringBuilder();
        for (String part : command) {
            commandStr.append(part).append(" ");
        }

        // any error message?
        StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream());

        // any output?
        StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream());

        // consume all output from err and out
        errorGobbler.start();
        outputGobbler.start();

        long now = System.currentTimeMillis();
        long timeoutInMillis = 60000L;
        long finish = now + timeoutInMillis;
        while (isAlive(proc) && (System.currentTimeMillis() < finish)) {
            Thread.sleep(10);
        }

        if (isAlive(proc)) {
            proc.destroy();
            errorGobbler.join();
            outputGobbler.join();
            return -1;
        }

        errorGobbler.join();
        outputGobbler.join();

        if (null != out && !out.equals(outputGobbler.getMessage().replace("\r", ""))) {
            System.out.println(commandStr + "\nExpected:\n" + out + "\n Got:\n" + outputGobbler.getMessage());
            return -2;
        }

        if (null != err && !err.equals(errorGobbler.getMessage().replace("\r", ""))) {
            System.out.println(commandStr + "\nExpected:\n" + err + "\n Got:\n" + errorGobbler.getMessage());
            return -3;
        }

        int retVal = proc.exitValue();
        if (retVal != 0 && err == null && out == null) {
            System.out.println(commandStr);
            System.out.println(outputGobbler.getMessage().replace("\r", ""));
            System.out.println(errorGobbler.getMessage().replace("\r", ""));
        }

        return retVal;
    }

    class StreamGobbler extends Thread {
        private final InputStream is;
        private String message;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public void run() {
            Scanner s = new Scanner(is);
            message = s.hasNext() ? s.useDelimiter("\\A").next().replace("\r", "") : "";
            s.close();
        }
    }
}
