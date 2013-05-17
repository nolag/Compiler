package cs444.codegen.x86;

import cs444.codegen.ISubtypeCellGen;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.DataInstructionMaker;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.InstructionArg.Size;

    public class SubtypeCellGen implements ISubtypeCellGen<X86Instruction> {
        public static final Size dataSize = Size.WORD;

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