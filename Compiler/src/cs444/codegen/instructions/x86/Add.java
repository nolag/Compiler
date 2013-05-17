package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Add extends BinInstruction{
    public Add(final Register addTo, final InstructionArg addWith, final X86SizeHelper sizeHelper){
        super("add", addTo, addWith, sizeHelper);
    }
}
