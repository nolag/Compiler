package cs444.codegen.x86;

import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.instructions.x86.bases.X86Instruction;

public class X86SubtypeIndexedTable extends SubtypeIndexedTable<X86Instruction> {
    public X86SubtypeIndexedTable(){
        super(new X86IndexedTableData(new SubtypeCellGen(), X86SizeHelper.getIntSize(SubtypeCellGen.dataSize)));
    }
}
