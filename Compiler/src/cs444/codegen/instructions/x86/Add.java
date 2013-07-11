package cs444.codegen.instructions.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;

public class Add extends BinInstruction{
    public Add(final Register addTo, final Memory addWith, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("add", addTo, addWith, sizeHelper, 2);
    }

    public Add(final Register addTo, final InstructionArg addWith, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("add", addTo, addWith, sizeHelper, 1);
    }

    public Add(final Memory addTo, final NotMemory addWith, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("add", addTo, addWith, sizeHelper, 3);
    }
}
