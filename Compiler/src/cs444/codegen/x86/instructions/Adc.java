package cs444.codegen.x86.instructions;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.BinInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Adc extends BinInstruction{
    public Adc(final Register addTo, final Memory addWith, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("adc", addTo, addWith, sizeHelper, 2);
    }

    public Adc(final Register addTo, final InstructionArg addWith, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("adc", addTo, addWith, sizeHelper, 1);
    }

    public Adc(final Memory addTo, final NotMemory addWith, final SizeHelper<X86Instruction, Size> sizeHelper){
        super("adc", addTo, addWith, sizeHelper, 3);
    }
}
