package cs444.codegen;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.types.APkgClassResolver;

public abstract class SelectorIndexedTable<T extends Instruction, E extends Enum<E>> {
    private final IndexedTableData<T, E> table;

    private final SizeHelper<T, E> sizeHelper;

    protected SelectorIndexedTable(final IndexedTableData<T, E> table, final SizeHelper<T, E> sizeHelper){
        this.table = table;
        this.sizeHelper = sizeHelper;
    }

    public final void addIndex(final String className, final String selector, final String methodImplLabel){
        table.addIndex(className, selector, methodImplLabel);
    }

    public final Map<String, String> addClass(final String fullName){
        return table.addColumn(fullName);
    }

    public final void addSelector(final String selector){
        table.addRow(selector);
    }

    public final long getOffset(final String selector){
        return table.getOffset(selector);
    }

    public final void generateSIT(final Platform<T, E> platform, final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException{

        for(final APkgClassResolver resolver : resolvers){
            if(resolver.shouldGenCode()) resolver.addToSelectorIndexedTable(this);
        }

        table.genCode(sizeHelper);
        if(outputFile) table.printCodeToFile(platform, directory + "_joos.sit.s");
    }
}