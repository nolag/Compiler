package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.UniInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

import java.util.Arrays;

public class Dd extends UniInstruction {
    public Dd(Immediate... data) {
        //Immediate won't need a sizeHelper
        super("dd", Arrays.asList((InstructionArg<X86Instruction, Size>[]) data), Size.DWORD, null, 0, 0);
    }
}
