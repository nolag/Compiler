package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.types.APkgClassResolver;

public class SelectorIndexedTable<T extends Instruction<T>, E extends Enum<E>> {
    private final IndexedTableData<T, E> table;
    private final Platform<T, E> platform;
    private final SizeHelper<T, E> sizeHelper;

    public SelectorIndexedTable(final Platform<T, E> platform) {
        this.platform = platform;
        sizeHelper = platform.getSizeHelper();
        table = new IndexedTableData<T, E>(platform, new SelectorCellGen());
    }

    public final void addIndex(final String className, final String selector, final String methodImplLabel) {
        table.addIndex(className, selector, methodImplLabel);
    }

    public final Map<String, String> addClass(final String fullName) {
        return table.addColumn(fullName);
    }

    public final void addSelector(final String selector) {
        table.addRow(selector);
    }

    public final long getOffset(final String selector) {
        return table.getOffset(selector);
    }

    public final void generateSIT(final List<APkgClassResolver> resolvers, final boolean outputFile) throws IOException {

        for (final APkgClassResolver resolver : resolvers) {
            if (resolver.shouldGenCode())
                resolver.addToSelectorIndexedTable(this);
        }

        table.genCode();
        if (outputFile)
            table.printCodeToFile(platform, platform.getOutputDir() + File.separator + "_joos.sit.s");
    }

    private class SelectorCellGen implements ICellGen<T, E> {
        public void genEmptyCelCode(String colHeaderLabel, String rowName, InstructionHolder<T> instructions) {
            instructions.add(platform.comment(colHeaderLabel + " does not have access to " + rowName + ":"));
            // Immediate won't need a sizeHelper so null is ok.
            instructions.addAll(sizeHelper.alloceDefaultCellSpace(platform.getNullStr()));
        }

        public void genCellCode(String colHeaderLabel, String rowName, String data, InstructionHolder<T> instructions) {
            instructions.add(platform.getExtern(data));
            // Immediate won't need a sizeHelper so null is ok.
            instructions.addAll(sizeHelper.alloceDefaultCellSpace(data));
        }

        @Override
        public long getCellSize() {
            return sizeHelper.getDefaultStackSize();
        }
    }

}