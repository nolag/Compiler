package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;

import cs444.codegen.SizeHelper;
import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.types.APkgClassResolver;

public class X86SubtypeIndexedTable extends SubtypeIndexedTable<X86Instruction, Size> {
    private static X86IndexedTableData makeData(){
        //NOTE word size won't change so x64 can use this to
        return new X86IndexedTableData(new SubtypeCellGen(), X86SizeHelper.sizeHelper32.getIntSize(SubtypeCellGen.dataSize));
    }

    public X86SubtypeIndexedTable(final List<APkgClassResolver> resolvers, final boolean outputFile,
            final String directory, final SizeHelper<X86Instruction, Size> sizeHelper) throws IOException{

        super(makeData(), resolvers, outputFile, directory, sizeHelper);
    }
}
