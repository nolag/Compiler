package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.X86SizeHelper;

public class And extends BinInstruction {
    public And(final InstructionArg addTo, final InstructionArg addWith, final X86SizeHelper sizeHelper){
        super("and", addTo, addWith, sizeHelper);
    }
}
