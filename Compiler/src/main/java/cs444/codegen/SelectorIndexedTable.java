package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.types.APkgClassResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SelectorIndexedTable<T extends Instruction<T>, E extends Enum<E>> {
    private final IndexedTableData<T, E> table;
    private final Platform<T, E> platform;
    private final SizeHelper<T, E> sizeHelper;

    public SelectorIndexedTable(Platform<T, E> platform) {
        this.platform = platform;
        sizeHelper = platform.getSizeHelper();
        table = new IndexedTableData<T, E>(platform, new SelectorCellGen());
    }

    public final void addIndex(String className, String selector, String methodImplLabel) {
        table.addIndex(className, selector, methodImplLabel);
    }

    public final Map<String, String> addClass(String fullName) {
        return table.addColumn(fullName);
    }

    public final void addSelector(String selector) {
        table.addRow(selector);
    }

    public final long getOffset(String selector) {
        return table.getOffset(selector);
    }

    public final void generateSIT(List<APkgClassResolver> resolvers, boolean outputFile) throws IOException {

        for (APkgClassResolver resolver : resolvers) {
            if (resolver.shouldGenCode()) {
                resolver.addToSelectorIndexedTable(this);
            }
        }

        table.genCode();
        if (outputFile) {
            table.printCodeToFile(platform, platform.getOutputDir() + File.separator + "_joos.sit.s");
        }
    }

    private class SelectorCellGen implements ICellGen<T, E> {
        public void genEmptyCelCode(String colHeaderLabel, String rowName, InstructionHolder<T> instructions) {
            instructions.add(platform.makeComment(colHeaderLabel + " does not have access to " + rowName + ":"));
            instructions.addAll(sizeHelper.alloceDefaultCellSpace(platform.getNullStr()));
        }

        public void genCellCode(String colHeaderLabel, String rowName, String data, InstructionHolder<T> instructions) {
            instructions.add(platform.makeExtern(data));
            instructions.addAll(sizeHelper.alloceDefaultCellSpace(data));
        }

        @Override
        public long getCellSize() {
            return sizeHelper.getDefaultStackSize();
        }
    }
}