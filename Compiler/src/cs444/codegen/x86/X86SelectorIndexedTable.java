package cs444.codegen.x86;

import cs444.codegen.ISubtypeCellGen;
import cs444.codegen.SelectorIndexedTable;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Dd;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;

public class X86SelectorIndexedTable extends SelectorIndexedTable<X86Instruction> {

    public X86SelectorIndexedTable(final X86SizeHelper sizeHelper){
        super(new X86IndexedTableData(new SelectorCellGen(), X86SizeHelper.getIntSize(sizeHelper.defaultStack)));
    }

    public static class SelectorCellGen implements ISubtypeCellGen<X86Instruction> {

        @Override
        public void genEmptyCelCode(final String colHeaderLabel, final String rowName,
                final InstructionHolder<X86Instruction> instructions) {

            instructions.add(new Comment(colHeaderLabel + " does not have access to " + rowName + ":"));
            //Immediate won't need a sizeHelper so null is ok.
            instructions.add(new Dd(Immediate.NULL));
        }

        @Override
        public void genCellCode(final String colHeaderLabel, final String rowName,
                final String data, final InstructionHolder<X86Instruction> instructions) {

            instructions.add(new Extern(data));
            //Immediate won't need a sizeHelper so null is ok.
            instructions.add(new Dd(new Immediate(data)));
        }
    }
}
