package cs444.acceptance;

import java.io.*;
import java.util.Scanner;

import cs444.Compiler;

    public class AsmAndLinkCallback implements ITestCallbacks{
        private static final int EXPECTED_DEFAULT_RTN_CODE = 123;

        @Override
        public boolean beforeCompile(final File _) throws IOException {
            final File folder = new File(Compiler.OUTPUT_DIRECTORY);

            if (!folder.exists()) folder.mkdir();

            for (final File file : folder.listFiles()) {
                if (file.getName().equals("runtime.s")) continue;

                if (!file.delete()){
                    System.err.println("Couldn't delete file: " + file.getAbsolutePath());
                }
            }
            return true;
        }

        @Override
        public boolean afterCompile(final File file) throws IOException, InterruptedException {
            final File folder = new File(Compiler.OUTPUT_DIRECTORY);
            if (!assembleOutput(folder)) return false;

            final String folderAbsPath = folder.getAbsolutePath();
            if(execAndWait(new String[] {"bash", "-c", "ld -melf_i386 -o main " +
                folderAbsPath + File.separator + "*.o"}) != 0) return false;

            final int returnCode = execAndWait(new String[] {"./main"});

            int expectedReturnCode;
            if (!file.isDirectory()){
                expectedReturnCode = EXPECTED_DEFAULT_RTN_CODE;
            }else{
                try{
                    expectedReturnCode = getExpectedReturnCode(file);
                }catch(final FileNotFoundException e){
                    expectedReturnCode = EXPECTED_DEFAULT_RTN_CODE;
                    //return true;
                }
            }

            if (expectedReturnCode != returnCode){
                System.out.println("\nWrong return code " + returnCode +
                        " expected: " + expectedReturnCode);
                System.out.println("In: " + file);
                return false;
            }
            return true;
        }

        private int getExpectedReturnCode(final File file)
                throws FileNotFoundException {
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

        private boolean assembleOutput(final File folder) throws IOException, InterruptedException {
            for (final File file : folder.listFiles()) {
                final String fileName = file.getName();
                if (!fileName.endsWith(".s")) continue;
                final String[] command = new String[] {"nasm", "-O1", "-f", "elf", "-g", "-F",
                        "dwarf", file.getAbsolutePath()};
                if (execAndWait(command) != 0) return false;
            }
            return true;
        }

        private int execAndWait(final String[] command) throws IOException, InterruptedException{
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

            errorGobbler.join();
            outputGobbler.join();

            return proc.waitFor();
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
