package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;

import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.types.APkgClassResolver;

public class X86SubtypeIndexedTable extends SubtypeIndexedTable<X86Instruction> {
    private static X86IndexedTableData makeData(){
        return new X86IndexedTableData(new SubtypeCellGen(), X86SizeHelper.getIntSize(SubtypeCellGen.dataSize));
    }

    public X86SubtypeIndexedTable(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException{
        super(makeData(), resolvers, outputFile, directory);
    }
}
