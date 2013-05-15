package cs444.codegen.x86;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.ICellCodeGenerator;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Dd;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.types.APkgClassResolver;

public class SelectorIndexedTable {
    private final IndexedTableData table = new IndexedTableData(new SelectorCellGen(), SizeHelper.getIntSize(SizeHelper.DEFAULT_STACK));

    public void addIndex(final String className, final String selector, final String methodImplLabel) {
        table.addIndex(className, selector, methodImplLabel);
    }

    public Map<String, String> addClass(final String fullName) {
        return table.addColumn(fullName);
    }

    public void addSelector(final String selector) {
        table.addRow(selector);
    }

    public long getOffset(final String selector){
        return table.getOffset(selector);
    }

    public static SelectorIndexedTable generateSIT(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException, FileNotFoundException {

        final SelectorIndexedTable sit = new SelectorIndexedTable();
        for(final APkgClassResolver resolver : resolvers){
            if(resolver.shouldGenCode()) resolver.addToSelectorIndexedTable(sit);
        }

        sit.table.genCode();

        if(outputFile){
            sit.table.printCodeToFile(directory + "_joos.sit.s");
        }

        return sit;
    }

    public class SelectorCellGen implements ICellCodeGenerator<X86Instruction> {

        @Override
        public void genEmptyCelCode(final String colHeaderLabel, final String rowName,
                final InstructionHolder<X86Instruction> instructions) {

            instructions.add(new Comment(colHeaderLabel + " does not have access to " + rowName + ":"));
            instructions.add(new Dd(Immediate.NULL));
        }

        @Override
        public void genCellCode(final String colHeaderLabel, final String rowName,
                final String data, final InstructionHolder<X86Instruction> instructions) {

            instructions.add(new Extern(data));
            instructions.add(new Dd(new Immediate(data)));
        }

    }
}
