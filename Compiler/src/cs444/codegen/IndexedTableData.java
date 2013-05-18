package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.peephole.InstructionPrinter;

public abstract class IndexedTableData<T extends Instruction> {

    public Map<String, Map<String, String>> indexedTable = new HashMap<String, Map<String,String>>();
    public Map<String, Long> offset = new LinkedHashMap<String, Long>();
    public long offsetCounter = 0;
    protected final InstructionHolder<T> instructions;
    protected final ISubtypeCellGen<T> cellGen;
    protected final long cellDataSize;

    public IndexedTableData(final ISubtypeCellGen<T> cellGen, final long cellDataSize) {
        this.cellGen = cellGen;
        this.cellDataSize = cellDataSize;
        instructions = new InstructionPrinter<T>();
    }

    public final void addIndex(final String columnName, final String rowName) {
        final Map<String, String> column = this.addColumn(columnName);
        column.put(rowName, ""); // data is not important
    }

    public final void addIndex(final String columnName, final String rowName, final String cellData) {
        final Map<String, String> column = this.addColumn(columnName);
        column.put(rowName, cellData);
    }

    public final Map<String, String> addColumn(final String columnName) {
        Map<String, String> column = indexedTable.get(columnName);
        if (column == null){
            column = new HashMap<String, String>();
            indexedTable.put(columnName, column);
        }
        return column;
    }

    public final void addRow(final String rowName) {
        if (!this.offset.containsKey(rowName)){
            this.offset.put(rowName, this.offsetCounter);
            this.offsetCounter += cellDataSize;
        }
    }

    public final long getOffset(final String rowName) {
        return offset.get(rowName);
    }

    public abstract void genCode();

    public final void printCodeToFile(final String filePath) throws IOException {
        final File file = new File(filePath);
        file.createNewFile();
        final PrintStream printer = new PrintStream(file);
        instructions.passToNext(printer);
        printer.close();
    }
}