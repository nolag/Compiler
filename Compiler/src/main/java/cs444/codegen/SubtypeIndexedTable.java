package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.types.APkgClassResolver;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SubtypeIndexedTable<T extends Instruction<T>, E extends Enum<E>> {
    private final IndexedTableData<T, E> table;
    private final Platform<T, E> platform;
    private final SizeHelper<T, E> sizeHelper;

    public SubtypeIndexedTable(Platform<T, E> platform, List<APkgClassResolver> resolvers,
                               boolean outputFile,
                               String directory) throws IOException {
        this.platform = platform;
        sizeHelper = platform.getSizeHelper();
        table = new IndexedTableData<T, E>(platform, new SubtypeCellGen());
        for (APkgClassResolver resolver : resolvers) {
            if (resolver.shouldGenCode()) {
                resolver.addToSubtypeIndexedTable(this);
            }
        }

        table.genCode();
        if (outputFile) {
            table.printCodeToFile(platform, directory + File.separator + "_joos.subtypeIT.s");
        }
    }

    public Map<String, String> addSubtype(String fullName) {
        return table.addColumn(fullName);
    }

    public void addSuperType(String className) {
        table.addRow(className);
    }

    public void addIndex(String subtypeITLbl, String superType) {
        table.addIndex(subtypeITLbl, superType);
    }

    public long getOffset(String superType) {
        return table.getOffset(superType);
    }

    private class SubtypeCellGen implements ICellGen<T, E> {
        @Override
        public void genEmptyCelCode(String colHeaderLabel, String rowName,
                                    InstructionHolder<T> instructions) {

            instructions.add(platform.makeComment(colHeaderLabel + " is not subtype of " + rowName));
            instructions.addAll(sizeHelper.alloceMinCellSpace(platform.getFalseStr()));
        }

        @Override
        public void genCellCode(String colHeaderLabel, String rowName, String data,
                                InstructionHolder<T> instructions) {

            instructions.add(platform.makeComment(colHeaderLabel + " is subtype of " + rowName));
            instructions.addAll(sizeHelper.alloceMinCellSpace(platform.getTrueStr()));
        }

        @Override
        public long getCellSize() {
            return sizeHelper.getMinSize();
        }
    }
}