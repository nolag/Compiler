package cs444.acceptance;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Asst1 {

    @Test
    public void testValidJoosCode() throws IOException, InterruptedException {
        String path = "JoosPrograms/SyntacticallyValidPrograms/";
        File folder = new File(path);

        int totalTests = 0;
        List<String> failFiles = new ArrayList<String>();
        for (File file : folder.listFiles()) {
            String fileName = file.getName();

            // Use this line to test a single file
            //if (!fileName.equals("StaticMethodDeclaration.java")) continue;

            if (file.isFile() && fileName.toLowerCase().endsWith(".java")){
                if (compileFile(path + fileName) == 0) {
                    System.out.print(".");
                }else{
                    System.out.print("F");
                    failFiles.add(fileName);
                }
                totalTests++;
            }
        }

        printSummary(totalTests, failFiles);
        int failures = failFiles.size();
        assertEquals("Compiler failed compiling " + failures + " files!", 0, failures);
    }

    private void printSummary(int totalTests, List<String> failFiles) {
        System.out.println("\nNumber of tests: " + totalTests);
        if (failFiles.size() != 0){
            System.out.println("Compailer failed compiling these files: ");
            for (String fileName: failFiles) {
                System.out.println("\t" + fileName);
            }
        }
    }

    private int compileFile(String filePath) throws IOException,
                                                    InterruptedException {
        Runtime rt = Runtime.getRuntime();
        Process process = rt.exec("java -cp bin cs444.Compiler " + filePath);

        int processId = process.waitFor();
        return processId;
    }
}
