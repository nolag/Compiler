package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class And extends BinInstruction {
    public And(final Register addTo, final InstructionArg addWith, final X86SizeHelper sizeHelper){
        super("and", addTo, addWith, sizeHelper, 1);
    }

    public And(final Memory addTo, final NotMemory addWith, final X86SizeHelper sizeHelper){
        super("and", addTo, addWith, sizeHelper, 3);
    }
}
