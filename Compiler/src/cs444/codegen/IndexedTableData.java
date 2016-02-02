package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;

public class IndexedTableData<T extends Instruction<T>, E extends Enum<E>> {

    private Map<String, Map<String, String>> indexedTable = new HashMap<String, Map<String, String>>();
    private Map<String, Long> offset = new LinkedHashMap<String, Long>();
    private long offsetCounter = 0;
    private final InstructionHolder<T> instructions;
    private final ICellGen<T, E> cellGen;
    private final Platform<T, E> platform;

    public IndexedTableData(Platform<T, E> platform, ICellGen<T, E> cellGen) {
        this.platform = platform;
        this.cellGen = cellGen;
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
        if (column == null) {
            column = new HashMap<String, String>();
            indexedTable.put(columnName, column);
        }
        return column;
    }

    public final void addRow(final String rowName) {
        if (!this.offset.containsKey(rowName)) {
            this.offset.put(rowName, this.offsetCounter);
            this.offsetCounter += cellGen.getCellSize();
        }
    }

    public final long getOffset(final String rowName) {
        return offset.get(rowName);
    }

    public void genCode() {
        // having data section in different files confuses GDB, needed to
        // uncomment this line because link.exe in windows needs it
        instructions.add(platform.getDataSection());

        for (final String colHeaderLabel : indexedTable.keySet()) {
            instructions.add(platform.makeGlobal(colHeaderLabel));
            instructions.add(platform.makeLabel(colHeaderLabel));

            final Map<String, String> column = indexedTable.get(colHeaderLabel);

            for (final String selector : offset.keySet()) {
                final String implLabel = column.get(selector);
                if (implLabel == null)
                    cellGen.genEmptyCelCode(colHeaderLabel, selector, instructions);
                else
                    cellGen.genCellCode(colHeaderLabel, selector, implLabel, instructions);
            }
        }
    }

    public final void printCodeToFile(final Platform<T, E> platform, final String filePath) throws IOException {
        final File file = new File(filePath);
        file.createNewFile();
        final PrintStream printer = new PrintStream(file);
        instructions.flush(platform, printer);
        printer.close();
    }
}