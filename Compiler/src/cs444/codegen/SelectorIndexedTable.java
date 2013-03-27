package cs444.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.types.APkgClassResolver;

public class SelectorIndexedTable {
    private final Map<String, Map<String, String>> sit = new HashMap<String, Map<String,String>>();;
    private final Map<String, Integer> offset = new HashMap<String, Integer>();
    private int offsetCounter = 0;

    public void addIndex(String className, String selector,
            String methodImplLabel) {

        Map<String, String> column = sit.get(className);
        if (column == null){
            column = new HashMap<String, String>();
            sit.put(className, column);
        }

        column.put(selector, methodImplLabel);
        offset.put(selector, offsetCounter);
        offsetCounter += 4;
    }

    public int getOffset(String selector){
        return offset.get(selector);
    }

    public void genCodeAndPrint(PrintStream printer) {
        List<Instruction> instructions = new LinkedList<Instruction>();

        // TODO: generate instructions here

        for(Instruction instruction : instructions) printer.println(instruction.generate());
    }

    public static SelectorIndexedTable generateSIT(List<APkgClassResolver> resolvers,
            boolean outputFile, String directory)
                    throws IOException, FileNotFoundException {
        SelectorIndexedTable sit = new SelectorIndexedTable();

        for(APkgClassResolver resolver : resolvers){
            resolver.addToSelectorIndexedTable(sit);
        }

        if(outputFile){
            File file = new File(directory + "_joos.sit.s");
            file.createNewFile();
            PrintStream printer = new PrintStream(file);
            sit.genCodeAndPrint(printer);
            printer.close();
        }

        return sit;
    }

}
