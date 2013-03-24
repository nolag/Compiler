package cs444.codegen.instructions;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.Register;

public class Or implements Instruction {
    public final Register addTo;
    public final Register addWith;

    public Or(Register addTo, Register addWith){
        this.addTo = addTo;
        this.addWith = addWith;
    }

    @Override
    public String generate() {
        return "or " + addTo.getValue(Size.DWORD) + ", " + addWith.getValue(Size.DWORD);
    }
}
