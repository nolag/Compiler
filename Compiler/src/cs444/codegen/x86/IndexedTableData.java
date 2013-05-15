package cs444.codegen.x86;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cs444.codegen.ICellCodeGenerator;
import cs444.codegen.instructions.x86.Global;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.peephole.InstructionPrinter;

public class IndexedTableData {
    public Map<String, Map<String, String>> indexedTable = new HashMap<String, Map<String,String>>();
    public Map<String, Long> offset = new LinkedHashMap<String, Long>();
    public long offsetCounter = 0;
    private final InstructionHolder<X86Instruction> instructions;
    private final ICellCodeGenerator<X86Instruction> cellGen;
    private final long cellDataSize;

    public IndexedTableData(final ICellCodeGenerator<X86Instruction> cellCodeGenerator, final long cellDataSize) {
        this.cellGen = cellCodeGenerator;
        this.cellDataSize = cellDataSize;
        instructions = new InstructionPrinter<X86Instruction>();
    }

    public void addIndex(final String columnName, final String rowName) {
        final Map<String, String> column = this.addColumn(columnName);
        column.put(rowName, ""); // data is not important
    }

    public void addIndex(final String columnName, final String rowName, final String cellData) {
        final Map<String, String> column = this.addColumn(columnName);
        column.put(rowName, cellData);
    }

    public Map<String, String> addColumn(final String columnName) {
        Map<String, String> column = indexedTable.get(columnName);
        if (column == null){
            column = new HashMap<String, String>();
            indexedTable.put(columnName, column);
        }
        return column;
    }

    public void addRow(final String rowName) {
        if (!this.offset.containsKey(rowName)){
            this.offset.put(rowName, this.offsetCounter);
            this.offsetCounter += cellDataSize;
        }
    }

    public long getOffset(final String rowName) {
        return this.offset.get(rowName);
    }

    public void genCode() {
        // having data section in different files confuses GDB :-/
        // instructions.add(new Section(SectionType.DATA));

        for (final String colHeaderLabel : indexedTable.keySet()) {
            instructions.add(new Global(colHeaderLabel));
            instructions.add(new Label(colHeaderLabel));

            final Map<String, String> column = indexedTable.get(colHeaderLabel);

            for (final String selector : offset.keySet()) {
                final String implLabel = column.get(selector);
                if (implLabel == null){
                    this.cellGen.genEmptyCelCode(colHeaderLabel, selector, instructions);
                }else{
                    this.cellGen.genCellCode(colHeaderLabel, selector, implLabel, instructions);
                }
            }
        }
    }

    public void printCodeToFile(final String filePath) throws IOException {
        final File file = new File(filePath);
        file.createNewFile();
        final PrintStream printer = new PrintStream(file);
        instructions.passToNext(printer);
        printer.close();
    }
}