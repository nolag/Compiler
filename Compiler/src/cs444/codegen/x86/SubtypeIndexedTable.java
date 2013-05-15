package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.ICellCodeGenerator;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.instructions.x86.factories.DataInstructionMaker;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.types.APkgClassResolver;

public class SubtypeIndexedTable {
    public class SubtypeCellGen implements ICellCodeGenerator<X86Instruction> {

        @Override
        public void genEmptyCelCode(final String colHeaderLabel,
                final String rowName, final InstructionHolder<X86Instruction> instructions) {

            instructions.add(new Comment(colHeaderLabel + " is not subtype of " + rowName));
            instructions.add(DataInstructionMaker.make(Immediate.FALSE, dataSize));
        }

        @Override
        public void genCellCode(final String colHeaderLabel, final String rowName,
                final String data, final InstructionHolder<X86Instruction> instructions) {
            instructions.add(new Comment(colHeaderLabel + " is subtype of " + rowName));
            instructions.add(DataInstructionMaker.make(Immediate.TRUE, dataSize));
        }

    }

    public final Size dataSize = Size.WORD;
    private final IndexedTableData table = new IndexedTableData(new SubtypeCellGen(), SizeHelper.getIntSize(dataSize));

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

    public static SubtypeIndexedTable generateTable(
            final List<APkgClassResolver> resolvers, final boolean outputFile,
            final String directory) throws IOException {

        final SubtypeIndexedTable sit = new SubtypeIndexedTable();

        for(final APkgClassResolver resolver : resolvers){
            if(resolver.shouldGenCode()) resolver.addToSubtypeIndexedTable(sit);
        }

        sit.table.genCode();

        if(outputFile){
            sit.table.printCodeToFile(directory + "_joos.subtypeIT.s");
        }

        return sit;
    }
}
