package cs444.acceptance;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

import cs444.Compiler;
import cs444.codegen.Platform;

    public class AsmAndLinkCallback implements ITestCallbacks{
        private static final int EXPECTED_DEFAULT_RTN_CODE = 123;

        //TODO this class's stuff can be in parallel.

        @Override
        public boolean beforeCompile(final File _) throws IOException {
            final File outputDir = new File(Compiler.OUTPUT_DIRECTORY);
            final File [] outputFolders = outputDir.listFiles();
            for(final File platformFolder : outputFolders){
                for (final File file : platformFolder.listFiles()) {
                    if (file.getName().equals("runtime.s") || file.getName().equals("runtime.asm")) continue;
                    if (!file.delete()){
                        System.err.println("Couldn't delete file: " + file.getAbsolutePath());
                    }
                }
            }
            return true;
        }

        @Override
        public boolean afterCompile(final File file, final Collection<Platform<?, ?>> platforms) throws IOException, InterruptedException {
            for(final Platform<?, ?> platform : platforms){
                final File folder = new File(platform.getOutputDir());
                if (!assembleOutput(folder, platform)) return false;

                final String folderAbsPath = folder.getAbsolutePath();

                if(execAndWait(platform.getLinkCmd(folderAbsPath)) != 0) return false;

                final int returnCode = execAndWait(platform.getExecuteCmd());

                int expectedReturnCode;
                if (!file.isDirectory()){
                    expectedReturnCode = EXPECTED_DEFAULT_RTN_CODE;
                }else{
                    //TODO get rid of the try catch here, only call if there is a file or return the expected return code
                    try{
                        expectedReturnCode = getExpectedReturnCode(file);
                    }catch(final FileNotFoundException e){
                        expectedReturnCode = EXPECTED_DEFAULT_RTN_CODE;
                    }
                }

                if (expectedReturnCode != returnCode){
                    System.out.println("\nWrong return code " + returnCode +
                            " expected: " + expectedReturnCode + " on platform " + platform.getClass().toString());
                    System.out.println("In: " + file);
                    return false;
                }
            }
            return true;
        }

        private int getExpectedReturnCode(final File file) throws FileNotFoundException {
            final File returnCodeFile = new File(file, "return.code");

            int expectedReturnCode;
            Scanner scan = null;
            try{
                scan = new Scanner(returnCodeFile);
                final String line = scan.nextLine();
                expectedReturnCode = Integer.parseInt(line);
            }finally{
                if(scan != null) scan.close();
            }
            return expectedReturnCode;
        }

        private boolean assembleOutput(final File folder, final Platform<?, ?> platform) throws IOException, InterruptedException {
            for (final File file : folder.listFiles()) {
                final String fileName = file.getAbsolutePath();
                if (!fileName.endsWith(".s")) continue;
                final String[] command = platform.getAssembleCmd(fileName);
                if (execAndWait(command) != 0) return false;
            }
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

        private int execAndWait(final String [] command) throws IOException, InterruptedException{
            final Process proc = Runtime.getRuntime().exec(command);

            // any error message?
            final StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            final StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), "OUTPUT");

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

            return proc.exitValue();
        }

        class StreamGobbler extends Thread{
            InputStream is;
            String type;
            boolean receivedOutput = false;

            public boolean receivedOutput() {
                return receivedOutput;
            }

            StreamGobbler(final InputStream is, final String type){
                this.is = is;
                this.type = type;
            }

            @Override
            public void run(){
                try{
                    final InputStreamReader isr = new InputStreamReader(is);
                    final BufferedReader br = new BufferedReader(isr);
                    String line=null;
                    while ( (line = br.readLine()) != null){
                        System.out.println(type + ">" + line);
                        receivedOutput = true;
                    }
                } catch (final IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }
