package cs444.codegen.instructions;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.Register;

public class And implements Instruction {
    public final Register addTo;
    public final Register addWith;

    public And(Register addTo, Register addWith){
        this.addTo = addTo;
        this.addWith = addWith;
    }

    @Override
    public String generate() {
        return "and " + addTo.getValue(Size.DWORD) + ", " + addWith.getValue(Size.DWORD);
    }
}
