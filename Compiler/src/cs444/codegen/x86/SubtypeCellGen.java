package cs444.codegen.x86;

import cs444.codegen.ISubtypeCellGen;
import cs444.codegen.SizeHelper;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.DataInstructionMaker;

    public class SubtypeCellGen implements ISubtypeCellGen<X86Instruction, Size> {
        public static final Size dataSize = Size.WORD;

        @Override
        public void genEmptyCelCode(final String colHeaderLabel,
                final String rowName, final InstructionHolder<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper) {

            instructions.add(new Comment(colHeaderLabel + " is not subtype of " + rowName));
            instructions.addAll(DataInstructionMaker.make(Immediate.FALSE, dataSize, sizeHelper));
        }

        @Override
        public void genCellCode(final String colHeaderLabel, final String rowName,
                final String data, final InstructionHolder<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper) {

            instructions.add(new Comment(colHeaderLabel + " is subtype of " + rowName));
            instructions.addAll(DataInstructionMaker.make(Immediate.TRUE, dataSize, sizeHelper));
        }
    }