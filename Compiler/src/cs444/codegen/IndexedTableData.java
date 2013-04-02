package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Global;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Label;
import cs444.codegen.instructions.Section;
import cs444.codegen.instructions.Section.SectionType;

public class IndexedTableData {
    public Map<String, Map<String, String>> indexedTable = new HashMap<String, Map<String,String>>();
    public Map<String, Integer> offset = new LinkedHashMap<String, Integer>();
    public int offsetCounter = 0;
    private List<Instruction> instructions = new LinkedList<Instruction>();
    private ICellCodeGenerator cellGen;
    private int cellDataSize;

    public IndexedTableData(ICellCodeGenerator cellCodeGenerator, int cellDataSize) {
        this.cellGen = cellCodeGenerator;
        this.cellDataSize = cellDataSize;
    }

    public void addIndex(String columnName, String rowName) {
        Map<String, String> column = this.addColumn(columnName);

        column.put(rowName, ""); // data is not important
    }

    public void addIndex(String columnName, String rowName,
            String cellData) {
        Map<String, String> column = this.addColumn(columnName);

        column.put(rowName, cellData);
    }

    public Map<String, String> addColumn(String columnName) {
        Map<String, String> column = indexedTable.get(columnName);
        if (column == null){
            column = new HashMap<String, String>();
            indexedTable.put(columnName, column);
        }
        return column;
    }

    public void addRow(String rowName) {
        if (!this.offset.containsKey(rowName)){
            this.offset.put(rowName, this.offsetCounter);
            this.offsetCounter += cellDataSize;
        }
    }

    public int getOffset(String rowName) {
        return this.offset.get(rowName);
    }


    public void genCode() {
        instructions.add(new Section(SectionType.DATA));

        for (String colHeaderLabel : indexedTable.keySet()) {
            instructions.add(new Global(colHeaderLabel));
            instructions.add(new Label(colHeaderLabel));

            Map<String, String> column = indexedTable.get(colHeaderLabel);

            for (String selector : offset.keySet()) {
                String implLabel = column.get(selector);
                if (implLabel == null){
                    this.cellGen.genEmptyCelCode(colHeaderLabel, selector, instructions);
                }else{
                    this.cellGen.genCellCode(colHeaderLabel, selector, implLabel, instructions);
                }
            }
        }
    }

    public void printCodeToFile(String filePath) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        PrintStream printer = new PrintStream(file);

        for(Instruction instruction : instructions) printer.println(instruction.generate());

        printer.close();
    }
}