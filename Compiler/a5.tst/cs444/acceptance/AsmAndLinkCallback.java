package cs444.acceptance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

    public class AsmAndLinkCallback implements ITestCallbacks{
        private static final String OUTPUT = "output/";

        @Override
        public boolean beforeCompile(File _) throws IOException {
            File folder = new File(OUTPUT);

            if (!folder.exists()) folder.mkdir();

            for (File file : folder.listFiles()) {
                if (file.getName().equals("runtime.s")) continue;

                if (!file.delete()){
                    System.err.println("Couldn't delete file: " + file.getAbsolutePath());
                }
            }
            return true;
        }

        @Override
        public boolean afterCompile(File file) throws IOException, InterruptedException {
            File folder = new File(OUTPUT);
            if (!assembleOutput(folder)) return false;

            String folderAbsPath = folder.getAbsolutePath();
            if(execAndWait(new String[] {"bash", "-c", "ld -melf_i386 -o main " +
                folderAbsPath + File.separator + "*.o"}) != 0) return false;

            int returnCode = execAndWait(new String[] {"./main"});

            if (!file.isDirectory()) return true;

            int expectedReturnCode;
            try{
                expectedReturnCode = getExpectedReturnCode(file);
            }catch(FileNotFoundException e){
                return true;
            }

            if (expectedReturnCode != returnCode){
                System.out.print("WRC" + returnCode + "->");
                return false;
            }
            return true;
        }

        private int getExpectedReturnCode(File file)
                throws FileNotFoundException {
            File returnCodeFile = new File(file, "return.code");

            int expectedReturnCode;
            Scanner scan = null;
            try{
                scan = new Scanner(returnCodeFile);
                String line = scan.nextLine();
                expectedReturnCode = Integer.parseInt(line);
            }finally{
                if(scan != null) scan.close();
            }
            return expectedReturnCode;
        }

        private boolean assembleOutput(File folder) throws IOException, InterruptedException {
            for (File file : folder.listFiles()) {
                String fileName = file.getName();
                if (!fileName.endsWith(".s")) continue;
                String[] command = new String[] {"nasm", "-O1", "-f", "elf", "-g", "-F",
                        "dwarf", file.getAbsolutePath()};
                if (execAndWait(command) != 0) return false;
            }
            return true;
        }

        private int execAndWait(String[] command) throws IOException, InterruptedException{
            Process proc = Runtime.getRuntime().exec(command);

            // any error message?
            StreamGobbler errorGobbler = new 
                StreamGobbler(proc.getErrorStream(), "ERROR");

            // any output?
            StreamGobbler outputGobbler = new 
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

            StreamGobbler(InputStream is, String type){
                this.is = is;
                this.type = type;
            }

            public void run(){
                try{
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line=null;
                    while ( (line = br.readLine()) != null){
                        System.out.println(type + ">" + line);
                        receivedOutput = true;
                    }
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }
