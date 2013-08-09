package cs444.codegen;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.types.APkgClassResolver;

public abstract class SubtypeIndexedTable <T extends Instruction, E extends Enum<E>> {
    private final IndexedTableData<T, E> table;

    protected SubtypeIndexedTable(final IndexedTableData<T, E> table, final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory, final SizeHelper<T, E> sizeHelper) throws IOException{
        this.table = table;
        generateTable(resolvers, outputFile, directory, sizeHelper);
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

    public void generateTable(final List<APkgClassResolver> resolvers, final boolean outputFile,
            final String directory, final SizeHelper<T, E> sizeHelper) throws IOException {


        for(final APkgClassResolver resolver : resolvers){
            if(resolver.shouldGenCode()) resolver.addToSubtypeIndexedTable(this);
        }

        table.genCode(sizeHelper);
        if(outputFile) table.printCodeToFile(directory + "_joos.subtypeIT.s");
    }
}