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
                if (!file.delete()){
                    System.err.println("Couldn't delete file: " + file.getAbsolutePath());
                }
            }
            return true;
        }

        @Override
        public boolean afterCompile(File file) throws IOException, InterruptedException {
            File folder = assembleOutput();

            String folderAbsPath = folder.getAbsolutePath();
            execAndWait(new String[] {"bash", "-c", "ld -melf_i386 -o main " +
                folderAbsPath + File.separator + "*.o"});

            int returnCode = execAndWait(new String[] {"./main"});

            if (!file.isDirectory()) return true;

            int expectedReturnCode = getExpectedReturnCode(file);

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

        private File assembleOutput() throws IOException, InterruptedException {
            File folder = new File(OUTPUT);
            for (File file : folder.listFiles()) {
                String fileName = file.getName();
                if (!fileName.endsWith(".s")) continue;
                String[] command = new String[] {"nasm", "-O1", "-f", "elf", "-g", "-F",
                        "dwarf", file.getAbsolutePath()};
                execAndWait(command);
            }
            return folder;
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

            return proc.waitFor();
        }

        class StreamGobbler extends Thread{
            InputStream is;
            String type;

            StreamGobbler(InputStream is, String type){
                this.is = is;
                this.type = type;
            }

            public void run(){
                try{
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line=null;
                    while ( (line = br.readLine()) != null)
                        System.out.println(type + ">" + line);
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }
