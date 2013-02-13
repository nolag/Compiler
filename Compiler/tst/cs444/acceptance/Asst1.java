package cs444.acceptance;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cs444.Compiler;

public class Asst1 {

    @Test
    public void testValidJoosCode() throws IOException, InterruptedException {
        String path = "JoosPrograms/SyntacticallyValidPrograms/";
        File folder = new File(path);

        int totalTests = 0;
        int filesSkipped = 0;
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
            }else{
                System.out.print("*"); // skip file
                filesSkipped++;
            }
        }

        printSummary(totalTests, filesSkipped, failFiles);
        int failures = failFiles.size();
        assertEquals("Compiler failed compiling " + failures + " files!", 0, failures);
    }

    private void printSummary(int totalTests, int filesSkipped, List<String> failFiles) {
        System.out.println("\nNumber of tests: " + totalTests);
        if(filesSkipped > 0) System.out.println("Number of files skipped: " + filesSkipped);
        if (failFiles.size() != 0){
            System.out.println("Compailer failed compiling these files: ");
            for (String fileName: failFiles) {
                System.out.println("\t" + fileName);
            }
        }
    }

    private int compileFile(String filePath) throws IOException,
                                                    InterruptedException {
        String[] files = new String[1];
        files[0] = filePath;
        return Compiler.compile(files);
    }
}
