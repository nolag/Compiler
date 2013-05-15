package cs444.codegen.instructions.x86;

import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.InstructionArg.Size;

public class Add implements X86Instruction {
    public final Register addTo;
    public final InstructionArg addWith;

    public Add(Register addTo, InstructionArg addWith){
        this.addTo = addTo;
        this.addWith = addWith;
    }

    @Override
    public String generate() {
        return "add " + addTo.getValue(Size.DWORD) + ", " + addWith.getValue(Size.DWORD);
    }
}
