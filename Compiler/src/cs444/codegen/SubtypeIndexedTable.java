package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.types.APkgClassResolver;

public class SubtypeIndexedTable<T extends Instruction<T>, E extends Enum<E>> {
    private final IndexedTableData<T, E> table;
    private final Platform<T, E> platform;
    private final SizeHelper<T, E> sizeHelper;

    public SubtypeIndexedTable(final Platform<T, E> platform, final List<APkgClassResolver> resolvers, final boolean outputFile,
            final String directory) throws IOException {
        this.platform = platform;
        sizeHelper = platform.getSizeHelper();
        this.table = new IndexedTableData<T, E>(platform, new SubtypeCellGen());
        for (final APkgClassResolver resolver : resolvers) {
            if (resolver.shouldGenCode())
                resolver.addToSubtypeIndexedTable(this);
        }

        table.genCode();
        if (outputFile)
            table.printCodeToFile(platform, directory + File.separator + "_joos.subtypeIT.s");
    }

    public Map<String, String> addSubtype(final String fullName) {
        return table.addColumn(fullName);
    }

    public void addSuperType(final String className) {
        table.addRow(className);
    }

    public void addIndex(final String subtypeITLbl, final String superType) {
        table.addIndex(subtypeITLbl, superType);
    }

    public long getOffset(final String superType) {
        return table.getOffset(superType);
    }

    private class SubtypeCellGen implements ICellGen<T, E> {
        @Override
        public void genEmptyCelCode(final String colHeaderLabel, final String rowName, final InstructionHolder<T> instructions) {

            instructions.add(platform.comment(colHeaderLabel + " is not subtype of " + rowName));
            instructions.addAll(sizeHelper.alloceMinCellSpace(platform.getFalseStr()));
        }

        @Override
        public void genCellCode(final String colHeaderLabel, final String rowName, final String data,
                final InstructionHolder<T> instructions) {

            instructions.add(platform.comment(colHeaderLabel + " is subtype of " + rowName));
            instructions.addAll(sizeHelper.alloceMinCellSpace(platform.getTrueStr()));
        }

        @Override
        public long getCellSize() {
            return sizeHelper.getMinSize();
        }
    }
}