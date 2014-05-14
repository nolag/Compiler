package cs444.codegen.x86.instructions;

import java.util.Arrays;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;


public class Dq extends UniInstruction{
    public Dq(final Immediate ... data) {
        //Immediate won't need a sizeHelper
        super("dq", Arrays.asList((InstructionArg<X86Instruction, Size>[])data), Size.QWORD, null, 0, 0);
    }
}
