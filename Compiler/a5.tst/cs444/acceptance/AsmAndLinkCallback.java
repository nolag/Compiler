package cs444.acceptance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;

import cs444.Compiler;
import cs444.codegen.OperatingSystem;
import cs444.codegen.Platform;

    public class AsmAndLinkCallback implements ITestCallbacks {
        //NOTE: change this if you are testing on another os or if you cahnge the exec/link to work multi os
        private static final String osToTest = "osx";
        
        private static final String EXEC = "main";
        private static final int EXPECTED_DEFAULT_RTN_CODE = 123;

        private static void deleteNonRuntime(final File root) {
            for (File child : root.listFiles()) {
                if (child.getName().equals("runtime.s") || child.getName().equals("runtime.asm")) continue;
                if (child.isDirectory()) {
                    deleteNonRuntime(child);
                    continue;
                }
                if (!child.delete()){
                    System.err.println("Couldn't delete file: " + child.getAbsolutePath());
                }
            }
        }
        
        //TODO this class's stuff can be in parallel, check if it's worth it.

        @Override
        public boolean beforeCompile(final File _) throws IOException {
            final File outputDir = new File(Compiler.OUTPUT_DIRECTORY);
            for(final File platformFolder : outputDir.listFiles()) {
                deleteNonRuntime(platformFolder);
            }
            return true;
        }

        @Override
        public boolean afterCompile(final File file, final Collection<Platform<?, ?>> platforms) throws IOException, InterruptedException {

            final String stdOut = getExpectedOutput(file, true);
            final String stdErr = getExpectedOutput(file, false);

            final int expectedReturnCode = getExpectedReturnCode(file);

            boolean pass = true;

            for(final Platform<?, ?> platform : platforms){
                OperatingSystem<?> os = null;
                
                for (OperatingSystem <?> tmp : platform.getOperatingSystems()) {
                    if (tmp.name.equals(osToTest)) {
                        os = tmp;
                        break;
                    }
                }
                
                final File folder = new File(platform.getOutputDir());
                if (!assembleOutput(folder, os)) return false;
                
                final String [] link = os.getLinkCmd(EXEC);
                if(execAndWait(link, null, null) != 0) {
                    System.out.println("********link failed!********");
                    for (String part : link) {
                        System.out.print(part + " ");
                    }
                    System.out.println();
                    return false;
                }

                final String [] command = os.getExecuteCmd(EXEC);

                final int returnCode = execAndWait(command, stdOut, stdErr);

                if (expectedReturnCode != returnCode){
                    System.out.println("\nWrong return code " + returnCode +
                            " expected: " + expectedReturnCode + " on platform " + platform.getClass().toString());
                    System.out.println("In: " + file);
                    pass = false;
                }
            }
            return pass;
        }

        private int getExpectedReturnCode(final File file) {
            final File returnCodeFile = new File(file, "return.code");
            int expectedReturnCode = EXPECTED_DEFAULT_RTN_CODE;

            if(!returnCodeFile.exists()) return EXPECTED_DEFAULT_RTN_CODE;

            try (Scanner scan = new Scanner(returnCodeFile)) {
                final String line = scan.nextLine();
                expectedReturnCode = Integer.parseInt(line);
            } catch (final FileNotFoundException e) { /*should never get here */ }

            return expectedReturnCode;
        }


        private String getExpectedOutput(final File file, final boolean output) {
            final File outputFile = new File(file, output ? "out.txt" : "err.txt");
            if(!outputFile.exists()) return "";
            try(final Scanner scanner = new Scanner(outputFile)){
                //Don't care about \r for testing because new lines may not match up the same for all systems.  Assume that the runtime is correct.
                return scanner.useDelimiter("\\A").next().replace("\r", "");
            } catch (final FileNotFoundException e) {
                //Should not get here
                e.printStackTrace();
                return "";
            }
        }

        private boolean assembleOutput(final File folder, final OperatingSystem<?> os) throws IOException, InterruptedException {
            for (final File file : folder.listFiles()) {
                final String fileName = file.getAbsolutePath();
                if (!fileName.endsWith(".s")) continue;
                final String[] command = os.getAssembleCmd(file);
                if (execAndWait(command, null, null) != 0) return false;
            }
            
            final String[] command = os.getAssembleCmd(os.getRuntimeFile());
            if (execAndWait(command, null, null) != 0) return false;
            
            return true;
        }

        private static boolean isAlive( final Process p ) {
            try
            {
                p.exitValue();
                return false;
            } catch (final IllegalThreadStateException e) {
                return true;
            }
        }

        private int execAndWait(final String [] command, final String out, final String err) throws InterruptedException, IOException {
            final Process proc = Runtime.getRuntime().exec(command);

            // any error message?
            final StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream());

            // any output?
            final StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream());

            // consume all output from err and out
            errorGobbler.start();
            outputGobbler.start();

            final long now = System.currentTimeMillis();
            final long timeoutInMillis = 60000L;
            final long finish = now + timeoutInMillis;
            while ( isAlive( proc ) && ( System.currentTimeMillis() < finish ) )
            {
                Thread.sleep( 10 );
            }

            if ( isAlive( proc ) )
            {
                proc.destroy();
                errorGobbler.join();
                outputGobbler.join();
                return -1;
            }

            errorGobbler.join();
            outputGobbler.join();

            if(null != out && !out.equals(outputGobbler.getMessage().replace("\r", ""))){
                System.out.println("Expected:\n" + out + "\n Got:\n" + outputGobbler.getMessage());
                return -2;
            }
            if(null != err && !err.equals(errorGobbler.getMessage().replace("\r", ""))){
                System.out.println("Expected:\n" + err + "\n Got:\n" + errorGobbler.getMessage());
                return -3;
            }
            
            return proc.exitValue();
        }

        class StreamGobbler extends Thread{
            private final InputStream is;
            private String message;

            public String getMessage(){
                return message;
            }

            StreamGobbler(final InputStream is){
                this.is = is;
            }

            @Override
            public void run(){
                final Scanner s = new Scanner(is);
                message = s.hasNext() ?  s.useDelimiter("\\A").next().replace("\r", "") : "";
                s.close();
            }
        }
    }
