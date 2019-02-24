package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class IndexedTableData<T extends Instruction<T>, E extends Enum<E>> {

    private final InstructionHolder<T> instructions;
    private final ICellGen<T, E> cellGen;
    private final Platform<T, E> platform;
    private Map<String, Map<String, String>> indexedTable = new HashMap<String, Map<String, String>>();
    private Map<String, Long> offset = new LinkedHashMap<String, Long>();
    private long offsetCounter = 0;

    public IndexedTableData(Platform<T, E> platform, ICellGen<T, E> cellGen) {
        this.platform = platform;
        this.cellGen = cellGen;
        instructions = new InstructionPrinter<T>();
    }

    public final void addIndex(String columnName, String rowName) {
        Map<String, String> column = addColumn(columnName);
        column.put(rowName, ""); // data is not important
    }

    public final void addIndex(String columnName, String rowName, String cellData) {
        Map<String, String> column = addColumn(columnName);
        column.put(rowName, cellData);
    }

    public final Map<String, String> addColumn(String columnName) {
        Map<String, String> column = indexedTable.get(columnName);
        if (column == null) {
            column = new HashMap<String, String>();
            indexedTable.put(columnName, column);
        }
        return column;
    }

    public final void addRow(String rowName) {
        if (!offset.containsKey(rowName)) {
            offset.put(rowName, offsetCounter);
            offsetCounter += cellGen.getCellSize();
        }
    }

    public final long getOffset(String rowName) {
        return offset.get(rowName);
    }

    public void genCode() {
        // having data section in different files confuses GDB, needed to
        // uncomment this line because link.exe in windows needs it
        instructions.add(platform.getDataSection());

        for (String colHeaderLabel : indexedTable.keySet()) {
            instructions.add(platform.makeGlobal(colHeaderLabel));
            instructions.add(platform.makeLabel(colHeaderLabel));

            Map<String, String> column = indexedTable.get(colHeaderLabel);

            for (String selector : offset.keySet()) {
                String implLabel = column.get(selector);
                if (implLabel == null) {
                    cellGen.genEmptyCelCode(colHeaderLabel, selector, instructions);
                } else {
                    cellGen.genCellCode(colHeaderLabel, selector, implLabel, instructions);
                }
            }
        }
    }

    public final void printCodeToFile(Platform<T, E> platform, String filePath) throws IOException {
        File file = new File(filePath);
        file.createNewFile();
        PrintStream printer = new PrintStream(file);
        instructions.flush(platform, printer);
        printer.close();
    }
}